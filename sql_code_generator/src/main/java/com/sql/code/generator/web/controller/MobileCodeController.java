package com.sql.code.generator.web.controller;

import com.sql.code.generator.configs.anotation.APIController;
import com.sql.code.generator.modules.common.service.CodeService;
import com.sql.code.generator.modules.common.service.CommonService;
import com.sql.code.generator.modules.common.service.MobileService;
import com.sven.common.lib.bean.CommonResponse;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author sven-ou
 */
@APIController
public class MobileCodeController {
	private static final Log log = LogFactory.getLog(MobileCodeController.class);

	@Autowired
	private MobileService mobileService;

	@RequestMapping(value = "/doAndroidServerAction", method = RequestMethod.GET)
	public @ResponseBody String doAndroidServerAction(
			String mobileActionUrl){
		return mobileService.getForEntity(mobileActionUrl, SourceFileInfo.class);
	}

	@RequestMapping(value = "/uploadOverwriteFile",method = RequestMethod.POST)
	public @ResponseBody
	CommonResponse uploadOverwriteFile(MultipartFile overwriteFile, String targetPath, String mobileActionUrl) {
		boolean success;
		try {
			String path = mobileService.saveFileToTemp(overwriteFile, targetPath);
			success = mobileService.uploadOverwriteFile(path, targetPath, mobileActionUrl);
		} catch (Exception e) {
			e.printStackTrace();
			return CommonResponse.failure(e.getMessage());
		}
		if(success){
			return CommonResponse.SIMPLE_SUCCESS;
		}else {
			return CommonResponse.SIMPLE_FAILURE;
		}
	}
}
