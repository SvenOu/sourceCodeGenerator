package com.sven.security.controller;

import com.sven.common.lib.bean.CommonResponse;
import com.sven.security.bean.CUserDetail;
import com.sven.security.config.APIController;
import com.sven.security.service.CsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@APIController
public class SecurityController {
	private static Log log = LogFactory.getLog(SecurityController.class);
	@Value("${com.sven.security.loginPageUrl}")
	private String loginPageUrl;

	@Autowired
	private CsService csService;
	
	@RequestMapping(value = "/getCurrentUserDetails")
	public @ResponseBody
    CommonResponse getCurrentUserDetails(HttpServletRequest request){
		CUserDetail csUser = csService.getCurrentUserDetails(request);
		if(null != csUser) {
			return CommonResponse.success(csUser);
		}
		return CommonResponse.SIMPLE_FAILURE;
	}


	@RequestMapping(value="/loginFail",method = RequestMethod.POST)
	public String loginFails(){
		return "redirect:" + loginPageUrl;
	}
}
