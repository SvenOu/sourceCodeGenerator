package com.sql.code.generator.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import com.sven.common.lib.config.GlobalAppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.client.RestTemplate;
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
        "classpath:sqlCodeGenerator/sql-code-generator.properties",
        "classpath:sqlCodeGenerator/database-jdbc.properties"
})
public class SqlCodeGeneratorConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态资源 url
        registry.addResourceHandler("/webs/sqlCodeGenerator/**")
                .addResourceLocations("classpath:/sqlCodeGenerator/static/");
    }

    @Autowired
    private GlobalAppConfig globalAppConfig;

    @Value("${jdbc.sqlite.driverClassName}")
    private String driverClassName;

    @Value("${jdbc.sqlite.username}")
    private String username;

    @Value("${jdbc.sqlite.password}")
    private String password;

    @Bean("sqliteDataSource")
    @Primary
    public DriverManagerDataSource driverManagerDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(globalAppConfig.getJdbcSqliteUrl());
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public TPEngine tPEngine() {
        return new TPEngine();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
