package com.sven.security.service;


import com.sven.security.bean.CUserDetail;

import javax.servlet.http.HttpServletRequest;

public interface CsService {

	public CUserDetail getCurrentUserDetails(HttpServletRequest request);
}
