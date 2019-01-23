package com.sven.common.lib.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("file:${user.dir}/_conf/app-config.properties")
public class GlobalAppConfig {
    @Value("${jdbc.sqlite.url}")
    private String jdbcSqliteUrl;

    @Value("${sql-code-templates.baseRoot}")
    private String sqlCodeTemplatesBaseRoot;

    @Value("${server.port}")
    private int serverPort;

    public String getJdbcSqliteUrl() {
        return jdbcSqliteUrl;
    }

    public String getSqlCodeTemplatesBaseRoot() {
        return sqlCodeTemplatesBaseRoot;
    }

    public int getServerPort() {
        return serverPort;
    }

    @Override
    public String toString() {
        return "GlobalAppConfig{" +
                "jdbcSqliteUrl='" + jdbcSqliteUrl + '\'' +
                ", sqlCodeTemplatesBaseRoot='" + sqlCodeTemplatesBaseRoot + '\'' +
                ", serverPort='" + serverPort + '\'' +
                '}';
    }
}
