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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String userDbFileDir = getUserTemplateFileDir();
        initDefaultTemplates();
        return FileUtils.getSourceFileInfo(userDbFileDir);
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
        String userDbFileDir = getUserTemplateFileDir();
        File userDbFileDirFile = new File(userDbFileDir);
        if(!userDbFileDirFile.exists()
                || null == userDbFileDirFile.listFiles()
                || userDbFileDirFile.listFiles().length <= 0){
            userDbFileDirFile.mkdirs();
            String userId = SecurityUtils.getCurrentUserDetails().getUsername();
            FileSystemUtils.copyRecursively(new File(defaultTemplateFileDirPath), userDbFileDirFile);
            File[] defTpls = new File(userDbFileDir + "/" + defaultTemplateDefaultFileName).listFiles();
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
    public String getUserDbFileDir() {
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        return dbFileDirPath + userId + '/';
    }

    @Override
    public String getUserTemplateFileDir() {
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        return templateFileDirPath + userId + '/';
    }
}
