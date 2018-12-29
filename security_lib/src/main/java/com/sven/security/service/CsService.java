package com.sven.security.service;


import com.sven.common.lib.bean.CommonResponse;
import com.sven.security.bean.CUserDetail;
import com.sven.security.vo.User;

import javax.servlet.http.HttpServletRequest;

public interface CsService {

	public CUserDetail getCurrentUserDetails(HttpServletRequest request);

	CommonResponse registerUser(User user);
}
