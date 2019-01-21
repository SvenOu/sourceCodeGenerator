package com.sql.code.generator.modules.common.service.impl;

import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.bean.DatasourceEnum;
import com.sql.code.generator.modules.common.bean.FileActionEnum;
import com.sql.code.generator.modules.common.dao.CodeTemplateDao;
import com.sql.code.generator.modules.common.dao.DataSourceDao;
import com.sql.code.generator.modules.common.service.CodeService;
import com.sql.code.generator.modules.common.service.FileService;
import com.sql.code.generator.modules.common.vo.CodeTemplate;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sven.common.lib.codetemplate.utils.FileUtils;
import com.sven.common.lib.codetemplate.utils.IdUtils;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeServiceImpl implements CodeService {
    private Log log = LogFactory.getLog(CodeServiceImpl.class);

    @Autowired
    private FileService fileService;

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
        String savePath = fileService.getUserDbFileDir() + dbFile.getOriginalFilename();
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
        dataSource.setUrl(savePath.replace(fileService.getUserBaseRootPath(), "/"));
        dataSource.setLock(false);
        dataSource.setOwner(SecurityUtils.getCurrentUserDetails().getUsername());
        dataSourceDao.save(dataSource);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public CommonResponse saveTemplateFile(MultipartFile tplFile, String fileName) throws IOException, ZipException {
        String destination = fileService.getUserTemplatePath() +  fileName + '/';

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
        String targetPath = destination
                .replaceAll("\\\\","/")
                .replaceAll(fileService.getUserBaseRootPath(), CodeTemplate.TEMPLATE_VIRTUAL_ROOT);
        codeTemplate.setPath(targetPath);
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
    public CommonResponse saveJsonDataSource(String dataSourceId, String type, String dataSourceName, String jsonData) {
        if(!DatasourceEnum.CUSTOM_JSON.getValue().equals(type)){
            throw new RuntimeException("do not support this type: " + type);
        }
        DataSource dbDataSource = null;
        if(!StringUtils.isEmpty(dataSourceId) ){
            dbDataSource = dataSourceDao.findByKey(dataSourceId);
        }
        if(dbDataSource != null){
            dbDataSource.setType(type);
            dbDataSource.setUrl("dataSourceName:" + dataSourceName);
            dbDataSource.setJsonData(jsonData);
            return CommonResponse.success(dataSourceDao.save(dbDataSource));
        }else {
            DataSource dataSource = new DataSource();
            dataSource.setDataSourceId(IdUtils.getId(DatasourceEnum.CUSTOM_JSON.getValue()));
            dataSource.setUrl("dataSourceName:" + dataSourceName);
            dataSource.setType(type);
            dataSource.setJsonData(jsonData);
            dataSource.setLock(false);
            dataSource.setOwner(SecurityUtils.getCurrentUserDetails().getUsername());
            return CommonResponse.success(dataSourceDao.save(dataSource));
        }
    }

    @Override
    public CommonResponse saveDbConfig(DataSource dataSource) {
        if(!DatasourceEnum.MSSQL.getValue().equals(dataSource.getType())){
            throw new RuntimeException("do not support this type: " + dataSource.getType());
        }
        DataSource dbDataSource = null;
        if(!StringUtils.isEmpty(dataSource.getDataSourceId())){
            dbDataSource = dataSourceDao.findByKey(dataSource.getDataSourceId());
        }
        if(dbDataSource != null){
            dbDataSource.setUserName(dataSource.getUserName());
            dbDataSource.setPassword(dataSource.getPassword());
            return CommonResponse.success(dataSourceDao.save(dbDataSource));
        }else {
            dataSource.setDataSourceId(IdUtils.getId(DatasourceEnum.MSSQL.getValue()));
            dataSource.setDriveClass(DatasourceEnum.MSSQL.getDriveClass());
            dataSource.setLock(false);
            dataSource.setOwner(SecurityUtils.getCurrentUserDetails().getUsername());
            return CommonResponse.success(dataSourceDao.save(dataSource));
        }
    }

    @Override
    public SourceFileInfo getUserDbFilesInfo() {
        String userDbFileDir = fileService.getUserDbFileDir();
        return FileUtils.getSourceFileInfo(userDbFileDir,
                fileService.getUserBaseRootPath(),
                SourceFileInfo.TEMPLATE_VIRTUAL_ROOT);
    }

    @Override
    public SourceFileInfo getTemplateFilesInfo() throws IOException {
        String userTplFileDir = fileService.getUserTemplateFileDir();
        initDefaultTemplates();
        return FileUtils.getSourceFileInfo(userTplFileDir,
                fileService.getUserBaseRootPath(),
                SourceFileInfo.TEMPLATE_VIRTUAL_ROOT);
    }

    @Override
    public CommonResponse deleteFile(String path) throws IOException {
        path = path.replace(SourceFileInfo.TEMPLATE_VIRTUAL_ROOT, fileService.getUserBaseRootPath());
        File file = new File(path);
        if(!file.exists()){
            return CommonResponse.failure("file： " + path + " not exit ");
        }
        FileSystemUtils.deleteRecursively(file);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public void initDefaultTemplates() throws IOException {
        String userTplFileDir = fileService.getUserTemplateFileDir();
        File usertplFilesDir = new File(userTplFileDir);
        if(!usertplFilesDir.exists()
                || null == usertplFilesDir.listFiles()
                || usertplFilesDir.listFiles().length <= 0){
            usertplFilesDir.mkdirs();
            String userId = SecurityUtils.getCurrentUserDetails().getUsername();
            FileSystemUtils.copyRecursively(new File(fileService.getDefaultTemplateRootPath()), usertplFilesDir);
            File[] defTpls = new File(userTplFileDir + "/" + fileService.getDefaultTemplateDirName()).listFiles();
            for(int i =0 ; i<defTpls.length; i++){
                File f= defTpls[i];
                CodeTemplate codeTemplate = new CodeTemplate();
                codeTemplate.setTemplateId(i + userId + "_"+fileService.getDefaultTemplateDirName() + "_" + f.getName());
                String path = (f.getAbsolutePath().replaceAll("\\\\","/") + '/')
                        .replace(fileService.getUserBaseRootPath(), CodeTemplate.TEMPLATE_VIRTUAL_ROOT);
                codeTemplate.setPath(path);
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
        path = path.replace(SourceFileInfo.TEMPLATE_VIRTUAL_ROOT, fileService.getUserBaseRootPath());
        FileCopyUtils.copy(content.getBytes(), new File(path));
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public CommonResponse resetDefaultUserTemplate() throws IOException {
        String userTplFileDir = fileService.getUserTemplateFileDir();
        File userTplFileDirFile = new File(userTplFileDir);
        FileSystemUtils.deleteRecursively(userTplFileDirFile);
        FileSystemUtils.copyRecursively(new File(fileService.getDefaultTemplateRootPath()), userTplFileDirFile);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public CommonResponse doFileAction(String path, String fileAction, String fileName) throws IOException {
        path = path.replace(SourceFileInfo.TEMPLATE_VIRTUAL_ROOT, fileService.getUserBaseRootPath());
        boolean success = false;
        if(FileActionEnum.EDIT_NAME.getName().equals(fileAction)){
            File oldfile = new File(path);
            File newfile = new File(path.replace(oldfile.getName(), fileName));
            success = oldfile.renameTo(newfile);
        }else
            if(FileActionEnum.NEW_FOLDER.getName().equals(fileAction)){
                String parentPath = new File(path).getParent();
                File newfile = new File(parentPath + "/" + fileName);
                success = newfile.mkdir();
        }else
            if(FileActionEnum.NEW_FILE.getName().equals(fileAction)){
                String parentPath = new File(path).getParent();
                File newfile = new File(parentPath + "/" + fileName);
                success = newfile.createNewFile();
        }else
            if(FileActionEnum.NEW_CHILD_FOLDER.getName().equals(fileAction)){
                File newfile = new File(path + "/" + fileName);
                success = newfile.mkdir();
        }else
            if(FileActionEnum.NEW_CHILD_FILE.getName().equals(fileAction)){
                File newfile = new File(path + "/" + fileName);
                success = newfile.createNewFile();
        }else
            if(FileActionEnum.DELETE.getName().equals(fileAction)){
                File newfile = new File(path);
                if(newfile.isFile()){
                    success = newfile.delete();
                }else {
                    success = FileSystemUtils.deleteRecursively(newfile);
                }
        }
        return success? CommonResponse.SIMPLE_SUCCESS: CommonResponse.SIMPLE_FAILURE;
    }
}
