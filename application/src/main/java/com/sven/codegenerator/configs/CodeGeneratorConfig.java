package com.sven.codegenerator.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CodeGeneratorConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        String loginPageUrl = "/webs/security/login.html";
        registry.addRedirectViewController("/",loginPageUrl);
//        registry.addViewController("/").setViewName(loginPageUrl);
 //                registry.addViewController("/302").setViewName("forward:/login.html");
//                registry.addViewController("/304").setViewName("forward:/login.html");
//                registry.addViewController("/403").setViewName("forward:/login.html");
//                registry.addViewController("/404").setViewName("forward:/login.html");
//                registry.addViewController("/500").setViewName("forward:/login.html");
    }
}
