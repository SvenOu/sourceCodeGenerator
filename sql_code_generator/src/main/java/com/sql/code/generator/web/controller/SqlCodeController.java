package com.sql.code.generator.web.controller;

import com.sql.code.generator.configs.anotation.APIController;
import com.sql.code.generator.modules.common.service.CodeService;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.bean.CommonResponse;
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
			String packageName,
			String dataSourceId,
			String templateId) throws IOException {
		return commonService.getCodeFileInfo(packageName, dataSourceId, templateId);
	}

	@RequestMapping(value = "/getUserRootDirCodeFileInfo", method = RequestMethod.GET)
	public @ResponseBody SourceFileInfo getUserRootDirCodeFileInfo() throws IOException {
		return commonService.getUserRootDirCodeFileInfo();
	}

	@RequestMapping(value = "/getSourceFileCode", method = RequestMethod.GET)
	public @ResponseBody String getSourceFileCode(String path) throws IOException {
		return commonService.getSourceFileCode(path);
	}

	@RequestMapping(value = "/getDoucumentFile", method = RequestMethod.GET)
	public @ResponseBody String getDoucumentFile() throws IOException {
		return commonService.getDoucumentFile();
	}

	@RequestMapping(path = "/downloadSourcesFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException {
		return commonService.downloadSourcesFile(path);
	}

	@RequestMapping(path = "/downloadSpecialFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadSpecialFile(String dataSourceId, String templateId) throws IOException, ZipException {
		String zipPath = commonService.generateDirZip(dataSourceId, templateId);
		return commonService.downloadSourcesFile(zipPath);
	}

	@RequestMapping(path = "/downloadAllFile", method = RequestMethod.GET)
	public ResponseEntity<Resource> downloadAllFile() throws IOException, ZipException {
		String zipPath = commonService.generateUserDirZip();
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

	@RequestMapping(value = "/uploadTemplateFile",method = RequestMethod.POST)
	public @ResponseBody CommonResponse uploadTemplateFile(MultipartFile templateFolderFile, String fileName) throws IOException, ZipException {
		return codeService.saveTemplateFile(templateFolderFile, fileName);
	}

	@RequestMapping(value = "/addRemoteDbConfig",method = RequestMethod.POST)
	public @ResponseBody CommonResponse addRemoteDbConfig(DataSource dataSource) throws IOException {
		return codeService.addRemoteDbConfig(dataSource);
	}

	@RequestMapping(value = "/deleteDataSource",method = RequestMethod.POST)
	public @ResponseBody CommonResponse deleteDataSource(String dataSourceId) throws IOException {
		return codeService.deleteDataSource(dataSourceId);
	}

	@RequestMapping(value = "/deleteCodeTemplate",method = RequestMethod.POST)
	public @ResponseBody CommonResponse deleteCodeTemplate(String templateId) throws IOException {
		return codeService.deleteCodeTemplate(templateId);
	}

	@RequestMapping(value = "/clearGenerateCode",method = RequestMethod.POST)
	public @ResponseBody CommonResponse clearGenerateCode() throws IOException {
		return commonService.clearGenerateCode();
	}

	@RequestMapping(value = "/deleteFile",method = RequestMethod.POST)
	public @ResponseBody CommonResponse deleteFile(String path) throws IOException {
		return codeService.deleteFile(path);
	}

	@RequestMapping(value = "/deleteUserTemplate",method = RequestMethod.POST)
	public @ResponseBody CommonResponse deleteUserTemplate(String path) throws IOException {
		return commonService.deleteUserTemplate(path);
	}

	@RequestMapping(value = "/getUserDbFilesInfo", method = RequestMethod.GET)
	public @ResponseBody SourceFileInfo getUserDbFilesInfo(){
		return codeService.getUserDbFilesInfo();
	}

    @RequestMapping(value = "/getAllTemplate", method = RequestMethod.GET)
    public @ResponseBody CommonResponse getAllTemplate() throws IOException {
        return codeService.getAllTemplate();
    }

	@RequestMapping(value = "/getTemplateFilesInfo", method = RequestMethod.GET)
	public @ResponseBody SourceFileInfo getTemplateFilesInfo() throws IOException {
		return codeService.getTemplateFilesInfo();
	}

	@RequestMapping(value = "/saveSourceFileCode", method = RequestMethod.POST)
	public @ResponseBody CommonResponse saveSourceFileCode(String path, String content) throws IOException {
		return codeService.saveSourceFileCode(path, content);
	}

	@RequestMapping(value = "/resetDefaultUserTemplate", method = RequestMethod.POST)
	public @ResponseBody CommonResponse resetDefaultUserTemplate() throws IOException {
		return codeService.resetDefaultUserTemplate();
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
