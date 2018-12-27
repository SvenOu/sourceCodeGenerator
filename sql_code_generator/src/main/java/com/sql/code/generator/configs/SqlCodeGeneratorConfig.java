package com.sql.code.generator.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
* 移除默认数据源配置
*/
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@Configuration
@PropertySource(value = {
        "classpath:sqlCodeGenerator/application.properties",
        "classpath:sqlCodeGenerator/sql-code-generator.properties"
})
public class SqlCodeGeneratorConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源 url
        registry.addResourceHandler("/webs/sqlCodeGenerator/**")
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
