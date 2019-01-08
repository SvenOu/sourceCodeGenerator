package com.sql.code.generator.modules.common.service;


import com.sql.code.generator.modules.common.vo.CodeTemplate;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CodeService {
	CommonResponse finAllDataSources(boolean excludeUserData);
	CommonResponse getAllDataSourceTypes();
	CommonResponse saveDbFile(MultipartFile dbFile, String type) throws IOException;

	CommonResponse deleteDataSource(String dataSourceId);

	CommonResponse addRemoteDbConfig(DataSource dataSource);

	SourceFileInfo getUserDbFilesInfo();

	SourceFileInfo getTemplateFilesInfo() throws IOException;

	CommonResponse deleteFile(String path) throws IOException;

	void initDefaultTemplates() throws IOException;

	CommonResponse getAllTemplate();

	CommonResponse deleteCodeTemplate(String templateId);

	String getUserDbFileDir();
	String getUserTemplateFileDir();
}
