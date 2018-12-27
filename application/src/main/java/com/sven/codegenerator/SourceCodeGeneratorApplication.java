package com.sven.codegenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//FIXME: 用到 spring 的 library 都要加上包名
@ComponentScan(basePackages = {
        "com.sven.codegenerator",
        "com.sql.code.generator",
        "com.sven.security",
        "com.sven.common.lib"
})
public class SourceCodeGeneratorApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SourceCodeGeneratorApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(SourceCodeGeneratorApplication.class, args);
    }
}
