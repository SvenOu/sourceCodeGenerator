package com.sven.common.lib.codetemplate.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@PropertySource(value = {
        "classpath:commonLib/application.properties"
})
public class CommonConfig implements WebMvcConfigurer {
//    @Bean
//    public WebMvcConfigurerAdapter forwardToIndex() {
//
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addViewControllers(ViewControllerRegistry registry) {
//                String loginPageUrl = "/webs/security/login.html";
//                //welcome page
//                registry.addViewController("/").setViewName("redirect:" + loginPageUrl);
//                //error pages
////                registry.addViewController("/302").setViewName("forward:/login.html");
////                registry.addViewController("/304").setViewName("forward:/login.html");
////                registry.addViewController("/403").setViewName("forward:/login.html");
////                registry.addViewController("/404").setViewName("forward:/login.html");
////                registry.addViewController("/500").setViewName("forward:/login.html");
//            }
//        };
//    }
}
