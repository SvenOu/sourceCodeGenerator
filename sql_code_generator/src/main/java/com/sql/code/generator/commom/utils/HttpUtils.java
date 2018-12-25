package com.sql.code.generator.commom.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * @author sven-ou
 */
public class HttpUtils {
    public static HttpSession getCurrentSession(){
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }

    public static String appentCurrentSession(String path){
        return path + File.separatorChar + getCurrentSession().getAttribute("userId");
    }
}
