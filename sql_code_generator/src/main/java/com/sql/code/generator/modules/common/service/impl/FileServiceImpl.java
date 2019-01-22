package com.sql.code.generator.modules.common.service.impl;

import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.service.FileService;
import com.sven.common.lib.config.GlobalAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {

    private String baseRootPath;

    @Value("${sql-code-generator.templates.default.root.path}")
    private String defaultTemplateRootPath;

    @Value("${sql-code-generator.templates.default.dir.name}")
    private String defaultTemplateDirName;

    @Value("${sql-code-templates.dir}")
    private String templatesDirPath;

    @Value("${sql-code-templates.doc.dir}")
    private String docFilePath;

    @Value("${sql-code-templates.default.name}")
    private String defaultTemplatesDirName;

    @Value("${sql-code-generator.dir}")
    private String generatorDirPath;

    @Value("${sql-code-templates.user.name}")
    private String templatesUserName;

    @Value("${sql-code-generator.templates.file.dir}")
    private String templateFileDirPath;

    @Value("${sql-code-generator.db.file.dir}")
    private String dbFileDirPath;

    @Autowired
    public FileServiceImpl(GlobalAppConfig globalAppConfig) {
        this.baseRootPath = globalAppConfig.getSqlCodeTemplatesBaseRoot();
    }

    @Override
    public String getUserGeneratorDirPath() {
        return getUserBaseRootPath() + generatorDirPath;
    }

    @Override
    public String getUserDbFileDir() {
        return getUserBaseRootPath() +  dbFileDirPath;
    }

    @Override
    public String getUserTemplateFileDir() {
        return getUserBaseRootPath() + templateFileDirPath;
    }

    @Override
    public String getUserTemplatePath() {
        String userTplFileDir = getUserTemplateFileDir();
        return userTplFileDir + templatesUserName + "/";
    }

    @Override
    public String getDefaultTemplateDirName() {
        return defaultTemplateDirName;
    }

    @Override
    public String getDefaultTemplateRootPath() {
        return getBaseRootPath() + defaultTemplateRootPath;
    }

    @Override
    public String getDocFilePath() {
        return getBaseRootPath() + docFilePath;
    }

    @Override
    public String getUserBaseRootPath() {
        return getBaseRootPath() + "UserFiles/"+ SecurityUtils.getCurrentUserId() + "/";
    }

    @Override
    public String getBaseRootPath() {
        return baseRootPath;
    }
}
