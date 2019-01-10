package com.sql.code.generator.modules.common.service.impl;

import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.bean.DatasourceEnum;
import com.sql.code.generator.modules.common.dao.CodeTemplateDao;
import com.sql.code.generator.modules.common.dao.DataSourceDao;
import com.sql.code.generator.modules.common.service.CodeService;
import com.sql.code.generator.modules.common.vo.CodeTemplate;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sven.common.lib.codetemplate.utils.FileUtils;
import com.sven.common.lib.codetemplate.utils.IdUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.UnzipParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;

@Service
public class CodeServiceImpl implements CodeService {
    private Log log = LogFactory.getLog(CodeServiceImpl.class);

    @Value("${sql-code-generator.db.file.dir}")
    private String dbFileDirPath;

    @Value("${sql-code-generator.templates.file.dir}")
    private String templateFileDirPath;

    @Value("${sql-code-generator.templates.default.file.dir}")
    private String defaultTemplateFileDirPath;

    @Value("${sql-code-generator.templates.default.file.name}")
    private String defaultTemplateDefaultFileName;

    @Value("${sql-code-templates.user.name}")
    private String templatesUserName;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Autowired
    private CodeTemplateDao codeTemplateDao;

    @Override
    public CommonResponse finAllDataSources(boolean excludeUserData) {
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        List<DataSource> dataSources = dataSourceDao.findAll(userId);
        if (excludeUserData) {
            for (DataSource ds : dataSources) {
                ds.setUserName("");
                ds.setPassword("");
            }
        }
        return CommonResponse.success(dataSources);
    }

    @Override
    public CommonResponse getAllDataSourceTypes() {
        List<DatasourceEnum> des = DatasourceEnum.getAll();
        List<Map> dataSources = new ArrayList<>(des.size());
        for (DatasourceEnum em : des) {
            Map ds = new HashMap();
            ds.put("type", em.getValue());
            dataSources.add(ds);
        }
        return CommonResponse.success(dataSources);
    }

    @Override
    public CommonResponse saveDbFile(MultipartFile dbFile, String type) throws IOException {
        if(!DatasourceEnum.SQLITE.getValue().equals(type)){
            throw new RuntimeException("do not support this type: " + type);
        }
        String savePath = getUserDbFileDir() + dbFile.getOriginalFilename() + '/';
        File newFile = new File(savePath);
        File newFileParent = newFile.getParentFile();
        if(!newFileParent.exists()){
            newFileParent.mkdirs();
        }
        FileCopyUtils.copy(dbFile.getBytes(), newFile);

        DataSource dataSource = new DataSource();
        dataSource.setDataSourceId(IdUtils.getId(DatasourceEnum.SQLITE.getValue()));
        dataSource.setDriveClass(DatasourceEnum.SQLITE.getDriveClass());
        dataSource.setType(type);
        dataSource.setUrl(DatasourceEnum.SQLITE.getUrlPrefix() + savePath);
        dataSource.setLock(false);
        dataSource.setOwner(SecurityUtils.getCurrentUserDetails().getUsername());
        dataSourceDao.save(dataSource);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public CommonResponse saveTemplateFile(MultipartFile tplFile, String fileName) throws IOException, ZipException {
        String destination = getUserTmeplatePath() +  fileName + '/';

        File tempRootDir = new File(destination + "temp");
        File tempFile = new File(tempRootDir.getAbsolutePath() + "/temp.zip");
        File tempDir = new File(tempRootDir.getAbsolutePath() + "/tempDir");
        if(!tempRootDir.exists()){
            tempRootDir.mkdirs();
        }
        //save file to temp
        FileCopyUtils.copy(tplFile.getBytes(), tempFile);

        // extract to temp
        ZipFile zipFile = new ZipFile(tempFile);
        zipFile.extractAll(tempDir.getPath());
        tempFile.delete();

        // check temp dir
        File[] childs = tempDir.listFiles();
        if(childs != null && childs.length > 0 && fileName.equals(childs[0].getName())){
            FileSystemUtils.copyRecursively(childs[0], new File(destination));
        }else {
            FileSystemUtils.copyRecursively(tempDir, new File(destination));
        }
        FileSystemUtils.deleteRecursively(tempRootDir);
        // insert to db
        CodeTemplate codeTemplate = new CodeTemplate();
        codeTemplate.setTemplateId(IdUtils.getId(CodeTemplate.TEMPLATE_PREFIX));
        codeTemplate.setPath(destination.replaceAll("\\\\","/") + '/');
        codeTemplate.setLock(false);
        codeTemplate.setOwner(SecurityUtils.getCurrentUserId());
        codeTemplateDao.save(codeTemplate);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public CommonResponse deleteDataSource(String dataSourceId) {
        DataSource dbSource = dataSourceDao.findByKey(dataSourceId);
        if(dbSource != null && dbSource.getLock()){
            return CommonResponse.failure("Cannot delete locked record.");
        }
        return CommonResponse.success(dataSourceDao.deleteByKey(dataSourceId));
    }

    @Override
    public CommonResponse addRemoteDbConfig(DataSource dataSource) {
        if(!DatasourceEnum.MSSQL.getValue().equals(dataSource.getType())){
            throw new RuntimeException("do not support this type: " + dataSource.getType());
        }
        dataSource.setDataSourceId(IdUtils.getId(DatasourceEnum.MSSQL.getValue()));
        dataSource.setDriveClass(DatasourceEnum.MSSQL.getDriveClass());
        String originUrl = dataSource.getUrl();
        if(!originUrl.contains(DatasourceEnum.MSSQL.getUrlPrefix())){
            dataSource.setUrl(DatasourceEnum.MSSQL.getUrlPrefix() + originUrl);
        }
        dataSource.setLock(false);
        dataSource.setOwner(SecurityUtils.getCurrentUserDetails().getUsername());
        return CommonResponse.success(dataSourceDao.save(dataSource));
    }

    @Override
    public SourceFileInfo getUserDbFilesInfo() {
        String userDbFileDir = getUserDbFileDir();
        return FileUtils.getSourceFileInfo(userDbFileDir);
    }

    @Override
    public SourceFileInfo getTemplateFilesInfo() throws IOException {
        String userTplFileDir = getUserTemplateFileDir();
        initDefaultTemplates();
        return FileUtils.getSourceFileInfo(userTplFileDir);
    }

    @Override
    public CommonResponse deleteFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists()){
            return CommonResponse.failure("file： " + path + " not exit ");
        }
        FileSystemUtils.deleteRecursively(file);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public void initDefaultTemplates() throws IOException {
        String userTplFileDir = getUserTemplateFileDir();
        File userDbFileDirFile = new File(userTplFileDir);
        if(!userDbFileDirFile.exists()
                || null == userDbFileDirFile.listFiles()
                || userDbFileDirFile.listFiles().length <= 0){
            userDbFileDirFile.mkdirs();
            String userId = SecurityUtils.getCurrentUserDetails().getUsername();
            FileSystemUtils.copyRecursively(new File(defaultTemplateFileDirPath), userDbFileDirFile);
            File[] defTpls = new File(userTplFileDir + "/" + defaultTemplateDefaultFileName).listFiles();
            for(int i =0 ; i<defTpls.length; i++){
                File f= defTpls[i];
                CodeTemplate codeTemplate = new CodeTemplate();
                codeTemplate.setTemplateId(i + userId + "_"+defaultTemplateDefaultFileName + "_" + f.getName());
                codeTemplate.setPath(f.getAbsolutePath().replaceAll("\\\\","/") + '/');
                codeTemplate.setLock(true);
                codeTemplate.setOwner(userId);
                codeTemplateDao.save(codeTemplate);
            }
        }
    }

    @Override
    public CommonResponse getAllTemplate() {
        try {
            initDefaultTemplates();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        return CommonResponse.success(codeTemplateDao.findAll(userId));
    }

    @Override
    public CommonResponse deleteCodeTemplate(String templateId) {
        CodeTemplate template = codeTemplateDao.findByKey(templateId);
        if(template != null && template.getLock()){
            return CommonResponse.failure("Cannot delete locked record.");
        }
        return CommonResponse.success(codeTemplateDao.deleteByKey(templateId));
    }

    @Override
    public CommonResponse saveSourceFileCode(String path, String content) throws IOException {
        FileCopyUtils.copy(content.getBytes(), new File(path));
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public CommonResponse resetDefaultUserTemplate() throws IOException {
        String userTplFileDir = getUserTemplateFileDir();
        File userDbFileDirFile = new File(userTplFileDir);
        FileSystemUtils.copyRecursively(new File(defaultTemplateFileDirPath), userDbFileDirFile);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public String getUserDbFileDir() {
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        return dbFileDirPath + userId + '/';
    }

    @Override
    public String getUserTemplateFileDir() {
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        return templateFileDirPath + userId + '/';
    }

    private String getUserTmeplatePath() {
        String userTplFileDir = getUserTemplateFileDir();
        return userTplFileDir + templatesUserName + "/";
    }
}
