package com.sql.code.generator.modules.common.service;


import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CodeService {
	CommonResponse finAllDataSources(boolean excludeUserData);
	CommonResponse getAllDataSourceTypes();
	CommonResponse saveDbFile(MultipartFile dbFile, String type) throws IOException;
	String getUserDbFileDir();

	CommonResponse deleteDataSource(String dataSourceId);
	CommonResponse addRemoteDbConfig(DataSource dataSource);
}
