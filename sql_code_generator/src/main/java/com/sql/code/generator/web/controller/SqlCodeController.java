package com.sql.code.generator.web.controller;

import com.sql.code.generator.configs.anotation.APIController;
import com.sql.code.generator.modules.common.service.CodeService;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.bean.InteractionContro;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author sven-ou
 */
@APIController
public class SqlCodeController {
	private static final Log log = LogFactory.getLog(SqlCodeController.class);

	@Autowired
	private CommonService commonService;

	@Autowired
	private CodeService codeService;

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

	/**  code generator start **/
	@RequestMapping(value = "/getDatasource",method = RequestMethod.POST)
	public @ResponseBody CommonResponse getDatasource(){
		return codeService.finAllDataSources(true);
	}

	@RequestMapping(value = "/getAllDataSourceTypes",method = RequestMethod.GET)
	public @ResponseBody CommonResponse getAllDataSourceTypes(){
		return codeService.getAllDataSourceTypes();
	}

	@RequestMapping(value = "/uploadDbFile",method = RequestMethod.POST)
	public @ResponseBody CommonResponse uploadDbFile(MultipartFile dbFile, String type) throws IOException {
		return codeService.saveDbFile(dbFile, type);
	}

	@RequestMapping(value = "/addRemoteDbConfig",method = RequestMethod.POST)
	public @ResponseBody CommonResponse addRemoteDbConfig(DataSource dataSource) throws IOException {
		return codeService.addRemoteDbConfig(dataSource);
	}

	@RequestMapping(value = "/deleteDataSource",method = RequestMethod.POST)
	public @ResponseBody CommonResponse deleteDataSource(String dataSourceId) throws IOException {
		return codeService.deleteDataSource(dataSourceId);
	}

	// FIXME: 不同 controller 交互
//	@RequestMapping(value="/testa", method=RequestMethod.GET)
//	public String testa(HttpServletRequest request){
//		log.error("--------------test a--------------------");
//		String toUrl = "forward:/api/controller/sqlCode/testc";
//		request.setAttribute(InteractionContro.KEY_CONTRO, new InteractionContro(toUrl));
//		return "forward:/api/controller/security/testb";
//	}
//
//	@RequestMapping(value="/testc", method=RequestMethod.GET)
//	public @ResponseBody CommonResponse testc(HttpServletRequest request){
//		log.error("--------------test c--------------------");
//		InteractionContro contro = (InteractionContro) request.getAttribute(InteractionContro.KEY_CONTRO);
//		log.error(contro);
//		return CommonResponse.success(contro.getData());
//	}
}
