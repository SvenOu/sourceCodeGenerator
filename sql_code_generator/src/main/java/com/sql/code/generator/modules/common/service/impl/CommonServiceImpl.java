package com.sql.code.generator.modules.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.bean.DatasourceEnum;
import com.sql.code.generator.modules.common.dao.CodeTemplateDao;
import com.sql.code.generator.modules.common.dao.DataSourceDao;
import com.sql.code.generator.modules.common.service.CodeGenerator;
import com.sql.code.generator.modules.common.composite.SqlType;
import com.sql.code.generator.modules.common.vo.CodeTemplate;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sql.code.generator.modules.common.service.CommonService;
import com.sven.common.lib.codetemplate.utils.FileUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    @Qualifier("sqlite")
    private CodeGenerator codeGenerator;

    @Autowired
    @Qualifier("mssql")
    private CodeGenerator sCodeGenerator;

    @Value("${sql-code-templates.dir}")
    private String templatesDirPath;

    @Value("${sql-code-templates.doc.dir}")
    private String docFilePath;

    @Value("${sql-code-templates.default.name}")
    private String defaultTemplatesDirName;

    @Value("${sql-code-generator.dir}")
    private String generatorDirPath;

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
        String path = null;
        if (DatasourceEnum.MSSQL.getValue().equalsIgnoreCase(dataSource.getType())) {
            rootContext = sCodeGenerator.generateCodeModel(packageName, driverClassName, url, username, password);
            path = sCodeGenerator.generateCodeFiles(rootContext, codeTpl.getPath(),
                    generatorDirPath + new File(codeTpl.getPath()).getName() + '/');
            return FileUtils.getSourceFileInfo(path);
        } else if (DatasourceEnum.SQLITE.getValue().equalsIgnoreCase(dataSource.getType())) {
            rootContext = codeGenerator.generateCodeModel(packageName, driverClassName, url, username, password);
            path = codeGenerator.generateCodeFiles(rootContext, codeTpl.getPath(),
                    generatorDirPath + new File(codeTpl.getPath()).getName() + '/');
            return FileUtils.getSourceFileInfo(path);
        }else {
            throw new RuntimeException("not support this dataSource: " + dataSource);
        }
    }

    @Override
    public ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException {
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
        String tplName = new File(tpl.getPath()).getName();
        String generatePath = generatorDirPath + TPConfig.KEY_USER_FILES + '/' + tplName + '/';
        String userName = SecurityUtils.getCurrentUserDetails().getUsername();
        // Initiate ZipFile object with the path/name of the zip file.
        String dirPath = generatePath + "tempFiles/";
        File dir = new File(dirPath);
        FileSystemUtils.deleteRecursively(dir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        ZipFile zipFile = new ZipFile(dirPath + "_" + tplName + "_" + userName +".zip");

        // Folder to add
        String folderToAdd = generatePath + userName +  '/';

        // Initiate Zip Parameters which define various properties such
        // as compression method, etc.
        ZipParameters parameters = new ZipParameters();

        // set compression method to store compression
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // Set the compression level
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Add folder to the zip file
        zipFile.addFolder(folderToAdd, parameters);

        return zipFile.getFile().getAbsolutePath();
    }

    @Override
    public String getDoucumentFile() throws IOException {
        return getSourceFileCode(docFilePath);
    }

    private void getFileNamesByDirPath(List<String> fileNames,  String path) {
        File folder = new File(path);
        if(!folder.exists()){
            return;
        }
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFileNamesByDirPath(fileNames, fileEntry.getAbsolutePath());
            } else {
                fileNames.add(fileEntry.getAbsolutePath());
            }
        }
    }

    @Override
    public String getSourceFileCode(String path) throws IOException {
        String ext = FileUtils.getFileExtension(new File(path));
        if(ext.equalsIgnoreCase("zip")){
            return "";
        }
        BufferedReader reader = new BufferedReader(new FileReader(path));
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
