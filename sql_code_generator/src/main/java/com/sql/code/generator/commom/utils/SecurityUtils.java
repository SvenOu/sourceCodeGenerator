package com.sql.code.generator.commom.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author sven-ou
 */
public class SecurityUtils {

    public static String appentCurrentSession(String path){
        String prefix = "anonymous";
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            UserDetails userDetails = (UserDetails) (principal instanceof UserDetails ? principal : null);
            prefix = userDetails.getUsername() + RequestContextHolder.currentRequestAttributes().getSessionId();
        }
        return path + '/' + prefix + '/';
    }

    public static String getCurrentSessionId(){
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    public static UserDetails getCurrentUserDetails(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            return (UserDetails) (principal instanceof UserDetails ? principal : null);
        }
        return null;
    }

}
