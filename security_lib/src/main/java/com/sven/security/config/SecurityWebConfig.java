package com.sven.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
* 移除默认数据源配置
*/
@Configuration
public class SecurityWebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源 url
        registry.addResourceHandler("/webs/security/**")
                .addResourceLocations("classpath:/security/static/");
    }
}
