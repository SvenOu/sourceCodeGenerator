package com.sql.code.generator.modules.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.bean.DatasourceEnum;
import com.sql.code.generator.modules.common.dao.CodeTemplateDao;
import com.sql.code.generator.modules.common.dao.DataSourceDao;
import com.sql.code.generator.modules.common.service.CodeGenerator;
import com.sql.code.generator.modules.common.service.CommonService;
import com.sql.code.generator.modules.common.service.FileService;
import com.sql.code.generator.modules.common.vo.CodeTemplate;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import com.sven.common.lib.codetemplate.utils.FileUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author sven-ou
 */
@Service
public class CommonServiceImpl implements CommonService {
    private static Log log = LogFactory.getLog(CommonServiceImpl.class);

    @Autowired
    private DataSourceDao dataSourceDao;
    @Autowired
    private CodeTemplateDao codeTemplateDao;

    @Autowired
    private FileService fileService;

    @Autowired
    @Qualifier("sqlite")
    private CodeGenerator codeGenerator;

    @Autowired
    @Qualifier("mssql")
    private CodeGenerator sCodeGenerator;

    @Autowired
    @Qualifier("mysql")
    private CodeGenerator mysqlCodeGenerator;


    @Autowired
    private TPEngine tpEngine;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public SourceFileInfo getCodeFileInfo(String packageName,
                                          String dataSourceId,
                                          String templateId) throws IOException {

        DataSource dataSource = dataSourceDao.findByKey(dataSourceId);
        CodeTemplate codeTpl = codeTemplateDao.findByKey(templateId);

        String driverClassName = dataSource.getDriveClass();
        String url = dataSource.getUrl();
        String username = dataSource.getUserName();
        String password = dataSource.getPassword();

        Map rootContext = null;
        String path = fileService.getUserGeneratorDirPath() + new File(getTplPath(codeTpl)).getName() + '/';
        if (DatasourceEnum.MSSQL.getValue().equalsIgnoreCase(dataSource.getType())) {
            url = DatasourceEnum.MSSQL.getUrlPrefix() + url;
            rootContext = sCodeGenerator.generateCodeModel(packageName, driverClassName, url, username, password);
            sCodeGenerator.generateCodeFiles(rootContext, getTplPath(codeTpl), path);
        }else
            if (DatasourceEnum.MYSQL.getValue().equalsIgnoreCase(dataSource.getType())) {
            url = DatasourceEnum.MYSQL.getUrlPrefix() + url;
            rootContext = mysqlCodeGenerator.generateCodeModel(packageName, driverClassName, url, username, password);
                mysqlCodeGenerator.generateCodeFiles(rootContext, getTplPath(codeTpl), path);
        }
        else
            if (DatasourceEnum.SQLITE.getValue().equalsIgnoreCase(dataSource.getType())) {
                url = DatasourceEnum.SQLITE.getUrlPrefix() + fileService.getUserBaseRootPath() + url;
                rootContext = codeGenerator.generateCodeModel(packageName, driverClassName, url, username, password);
                codeGenerator.generateCodeFiles(rootContext, getTplPath(codeTpl), path);
        }else
            if (DatasourceEnum.CUSTOM_JSON.getValue().equalsIgnoreCase(dataSource.getType())) {
                Map context = mapper.readValue(dataSource.getJsonData(), Map.class);
                tpEngine.progressAll(getTplPath(codeTpl), path, context);
        }
        else {
            throw new RuntimeException("not support this dataSource: " + dataSource);
        }
        return getUserGenerateRootCodeFileInfo();
    }

    @Override
    public ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException {
        path = path.replace(SourceFileInfo.TEMPLATE_VIRTUAL_ROOT, fileService.getUserBaseRootPath());
        File file = new File(path);
        if(!file.exists() || file.isDirectory()){
            return ResponseEntity.notFound().build();
        }
        Path fPath = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\""+ file.getName() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @Override
    public String generateDirZip(String dataSourceId, String templateId) throws ZipException, IOException {
        CodeTemplate tpl = codeTemplateDao.findByKey(templateId);
        String tplName = new File(getTplPath(tpl)).getName();
        String generatePath = fileService.getUserBaseRootPath() + tplName + '/';
        String userName = SecurityUtils.getCurrentUserDetails().getUsername();
        // Initiate ZipFile object with the path/name of the zip file.
        String dirPath = fileService.getUserGeneratorDirPath() + "z_tempFiles/";
        File dir = new File(dirPath);
        FileSystemUtils.deleteRecursively(dir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        ZipFile zipFile = new ZipFile(dirPath + "_" + tplName + "_" + userName +".zip");

        // Initiate Zip Parameters which define various properties such
        // as compression method, etc.
        ZipParameters parameters = new ZipParameters();

        // set compression method to store compression
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // Set the compression level
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Add folder to the zip file
        zipFile.addFolder(generatePath, parameters);

        return zipFile.getFile().getAbsolutePath();
    }

    private String getTplPath(CodeTemplate codeTemplate){
        return codeTemplate.getPath()
                .replace(CodeTemplate.TEMPLATE_VIRTUAL_ROOT, fileService.getUserBaseRootPath());
    }
    @Override
    public String generateUserDirZip() throws ZipException {

        String generatePath = fileService.getUserGeneratorDirPath();
        String userName = SecurityUtils.getCurrentUserId();
        // Initiate ZipFile object with the path/name of the zip file.
        String dirPath = fileService.getUserBaseRootPath() + "z_tempFiles/";
        File dir = new File(dirPath);
        FileSystemUtils.deleteRecursively(dir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        ZipFile zipFile = new ZipFile(dirPath + "_"  + userName +"_GenerateCodes.zip");

        // Initiate Zip Parameters which define various properties such
        // as compression method, etc.
        ZipParameters parameters = new ZipParameters();

        // set compression method to store compression
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // Set the compression level
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Add folder to the zip file
        zipFile.addFolder(generatePath, parameters);

        return zipFile.getFile().getAbsolutePath();
    }

    @Override
    public String getDoucumentFile() throws IOException {
        return getSourceFileCode(fileService.getDocFilePath());
    }

    @Override
    public SourceFileInfo getUserGenerateRootCodeFileInfo() {
        return FileUtils.getSourceFileInfo(fileService.getUserGeneratorDirPath(),
                fileService.getUserBaseRootPath(),
                SourceFileInfo.TEMPLATE_VIRTUAL_ROOT);
    }

    @Override
    public CommonResponse clearGenerateCode() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(fileService.getUserGeneratorDirPath()));
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public String downloadAllTemplateFile() throws IOException, ZipException {
        String targetDir = fileService.getUserTemplateFileDir();
        String userName = SecurityUtils.getCurrentUserId();
        // Initiate ZipFile object with the path/name of the zip file.
        String dirPath = fileService.getUserBaseRootPath() + "z_tempFiles/";
        File dir = new File(dirPath);
        FileSystemUtils.deleteRecursively(dir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        ZipFile zipFile = new ZipFile(dirPath + "_"  + userName +"_TemplateCodes.zip");

        // Initiate Zip Parameters which define various properties such
        // as compression method, etc.
        ZipParameters parameters = new ZipParameters();

        // set compression method to store compression
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // Set the compression level
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Add folder to the zip file
        zipFile.addFolder(targetDir, parameters);

        return zipFile.getFile().getAbsolutePath();
    }
//    private void getFileNamesByDirPath(List<String> fileNames,  String path) {
//        File folder = new File(path);
//        if(!folder.exists()){
//            return;
//        }
//        for (File fileEntry : folder.listFiles()) {
//            if (fileEntry.isDirectory()) {
//                getFileNamesByDirPath(fileNames, fileEntry.getAbsolutePath());
//            } else {
//                fileNames.add(fileEntry.getAbsolutePath());
//            }
//        }
//    }
    @Override
    public String getSourceFileCode(String path) throws IOException {
        path = path.replace(SourceFileInfo.TEMPLATE_VIRTUAL_ROOT, fileService.getUserBaseRootPath());
        File file = new File(path);
        return getSourceFileCode(file);
    }

    @Override
    public String getSourceFileCode(File file) throws IOException {
        if(file.length() > 5 * 1024 * 1024 || !file.isFile()){
            return file.getPath() + ": size greater than 5M";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8));
        StringBuilder builder = new StringBuilder();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            builder.append(currentLine);
            builder.append("\n");
            currentLine = reader.readLine();
        }
        reader.close();
        return builder.toString();
    }
}
