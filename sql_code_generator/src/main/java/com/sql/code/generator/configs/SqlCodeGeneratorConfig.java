package com.sql.code.generator.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.code.generator.commom.HttpRequestInterceptor;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ImportResource(value = {
        "classpath:sqlCodeGenerator/database-context.xml"
})
@PropertySource(value = {
        "classpath:sqlCodeGenerator/application.properties",
        "classpath:sqlCodeGenerator/database-jdbc.properties",
        "classpath:sqlCodeGenerator/sql-code-generator.properties"
})
public class SqlCodeGeneratorConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        HttpRequestInterceptor httpRequestInterceptor = new HttpRequestInterceptor();
        registry.addInterceptor(httpRequestInterceptor)
                .addPathPatterns("/**/controller/**/");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源 url
        registry.addResourceHandler("/sqlCodeGenerator/**")
                .addResourceLocations("classpath:/sqlCodeGenerator/static/");
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TPEngine tPEngine() {
        return new TPEngine();
    }
}
