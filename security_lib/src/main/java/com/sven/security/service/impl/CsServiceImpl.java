package com.sven.security.service.impl;

import com.sven.common.lib.bean.CommonResponse;
import com.sven.security.bean.CUserDetail;
import com.sven.security.bean.RoleEnum;
import com.sven.security.dao.UserDao;
import com.sven.security.dao.UserRoleDao;
import com.sven.security.service.CsService;
import com.sven.security.vo.User;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
@Repository
public class CsServiceImpl implements CsService {
	private Log log = LogFactory.getLog(CsServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public CUserDetail getCurrentUserDetails(HttpServletRequest request) {
	    return this.getUserDeatils();
	}

	@Override
	public CommonResponse registerUser(User user) {
		User dbUser = userDao.findByKey(user.getUserId());
		if(null == dbUser){
			user.setRoles(RoleEnum.ROLE_PREFIX + RoleEnum.USER.getValue());
			user.setActive(1);
			int result = userDao.insert(user);
			return result > 0 ? CommonResponse.SIMPLE_SUCCESS : CommonResponse.SIMPLE_FAILURE;
		}else {
			return CommonResponse.failure("This user has already registered");
		}
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
