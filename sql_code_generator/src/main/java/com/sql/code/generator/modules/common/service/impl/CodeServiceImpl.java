package com.sql.code.generator.modules.common.service.impl;

import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.bean.DatasourceEnum;
import com.sql.code.generator.modules.common.dao.DataSourceDao;
import com.sql.code.generator.modules.common.service.CodeService;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CodeServiceImpl implements CodeService {
    private Log log = LogFactory.getLog(CodeServiceImpl.class);

    @Value("${sql-code-generator.db.file.dir}")
    private String dbFileDirPath;

    @Autowired
    private DataSourceDao dataSourceDao;

    @Override
    public CommonResponse finAllDataSources(boolean excludeUserData) {
        List<DataSource> dataSources = dataSourceDao.findAll();
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
        dataSourceDao.save(dataSource);
        return CommonResponse.SIMPLE_SUCCESS;
    }

    @Override
    public String getUserDbFileDir() {
        String userId = SecurityUtils.getCurrentUserDetails().getUsername();
        return dbFileDirPath + userId + '/';
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
        dataSource.setLock(false);
        return CommonResponse.success(dataSourceDao.save(dataSource));
    }
}
