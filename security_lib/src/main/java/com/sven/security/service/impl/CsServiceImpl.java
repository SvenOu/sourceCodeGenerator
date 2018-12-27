package com.sven.security.service.impl;

import com.sven.security.bean.CUserDetail;
import com.sven.security.service.CsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
@Repository
public class CsServiceImpl implements CsService {
	private Log log = LogFactory.getLog(CsServiceImpl.class);

	@Override
	public CUserDetail getCurrentUserDetails(HttpServletRequest request) {
	    return this.getUserDeatils();
	}

	private CUserDetail getUserDeatils(){
		SecurityContext securityContext = SecurityContextHolder.getContext();
	    Authentication authentication = securityContext.getAuthentication();
	    if (authentication != null) {
	        Object principal = authentication.getPrincipal();
			CUserDetail userDetails = (CUserDetail) (principal instanceof CUserDetail ? principal : null);
	        return userDetails;
	    }
		return null;
	}
	
}
