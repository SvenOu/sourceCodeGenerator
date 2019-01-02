package com.sql.code.generator.web.controller;

import com.sql.code.generator.configs.anotation.APIController;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sql.code.generator.modules.common.service.CommonService;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * @author sven-ou
 */
@APIController
public class SqlCodeController {
	private static final Log log = LogFactory.getLog(SqlCodeController.class);

	@Autowired
	private CommonService commonService;

	@RequestMapping(value = "/getCodeFileInfo", method = RequestMethod.GET)
	public @ResponseBody SourceFileInfo getCodeFileInfo(
			String type,
			String packageName,
			String url,
			String username,
			String password) throws IOException {
		return commonService.getCodeFileInfo(type, packageName, url, username, password);
	}

	@RequestMapping(value = "/getSourceFileCode", method = RequestMethod.GET)
	public @ResponseBody String getSourceFileCode(String path) throws IOException {
		return commonService.getSourceFileCode(path);
	}

	@RequestMapping(path = "/downloadSourcesFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException {
		return commonService.downloadSourcesFile(path);
	}

	@RequestMapping(path = "/downloadAllFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadAllFile(String type) throws IOException, ZipException {
		String zipPath = commonService.generateDirZip(type);
		return commonService.downloadSourcesFile(zipPath);
	}
}
