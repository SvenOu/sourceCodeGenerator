package com.sven.security.service.authentic;

import com.sven.security.bean.CUserDetail;
import com.sven.security.dao.UserDao;
import com.sven.security.dao.UserRoleDao;
import com.sven.security.vo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("CUserDetailsService")
public class CUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CUserDetailsService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Override
    public CUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Authenticating user with username: " + username);
        User user = userDao.findByKey(username);
        if(null == user){
            String error = "cannot find "+ username+ " in db";
            log.error(error);
            throw new UsernameNotFoundException(error);
        }
        if(user.getActive() != 1){
            String error = "username: " + username+ " is not active user";
            log.error(error);
            throw new UsernameNotFoundException(error);
        }
        String roles = user.getRoles();
        if(null == roles){
            throw new UsernameNotFoundException(username+ " is not a legal user");
        }
        return new CUserDetail(user);
    }

}
