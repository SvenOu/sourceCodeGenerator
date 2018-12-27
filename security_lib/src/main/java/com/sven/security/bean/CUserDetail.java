package com.sven.security.bean;

import com.sven.security.vo.User;
import org.springframework.security.core.authority.AuthorityUtils;

public class CUserDetail extends org.springframework.security.core.userdetails.User {

    private User user;

    public CUserDetail(User user) {
        super(user.getUserId(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRoles().split(",")));
        this.user = user;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
