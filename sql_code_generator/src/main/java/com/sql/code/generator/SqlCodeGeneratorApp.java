package com.sql.code.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Description;

@Description("just for test (仅仅提供测试)")
@SpringBootApplication
public class SqlCodeGeneratorApp{
    public static void main(String[] args) {
        SpringApplication.run(SqlCodeGeneratorApp.class, args);
    }
}
