package com.sql.code.generator.modules.common.service;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import net.lingala.zip4j.exception.ZipException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CodeService {

	CommonResponse finAllDataSources(boolean excludeUserData);

	CommonResponse getAllDataSourceTypes();

	CommonResponse saveDbFile(MultipartFile dbFile, String type) throws IOException;

	CommonResponse saveTemplateFile(MultipartFile tplFile, String fileName) throws IOException, ZipException;

	CommonResponse deleteDataSource(String dataSourceId);

	CommonResponse addRemoteDbConfig(DataSource dataSource);

	SourceFileInfo getUserDbFilesInfo();

	SourceFileInfo getTemplateFilesInfo() throws IOException;

	CommonResponse deleteFile(String path) throws IOException;

	void initDefaultTemplates() throws IOException;

	CommonResponse getAllTemplate();

	CommonResponse deleteCodeTemplate(String templateId);

	CommonResponse saveSourceFileCode(String path, String content) throws IOException;

	CommonResponse resetDefaultUserTemplate() throws IOException;

	String getUserDbFileDir();
	String getUserTemplateFileDir();
}
