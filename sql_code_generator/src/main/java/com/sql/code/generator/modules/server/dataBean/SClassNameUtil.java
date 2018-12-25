package com.sql.code.generator.modules.server.dataBean;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Map;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class SClassNameUtil {
    public static final ClassName NamedParameterJdbcDaoSupport = ClassName.get("org.springframework.jdbc.core.namedparam", "NamedParameterJdbcDaoSupport");
    public static final ClassName RowMapper = ClassName.get("org.springframework.jdbc.core", "RowMapper");
    public static final ClassName Log = ClassName.get("org.apache.commons.logging", "Log");
    public static final ClassName LogFactory = ClassName.get("org.apache.commons.logging", "LogFactory");
    public static final ClassName BeanPropertyRowMapper = ClassName.get("org.springframework.jdbc.core", "BeanPropertyRowMapper");
    public static final ClassName BeanPropertySqlParameterSource = ClassName.get("org.springframework.jdbc.core.namedparam", "BeanPropertySqlParameterSource");
    public static final ParameterizedTypeName MapStringString = ParameterizedTypeName.get(Map.class, String.class, String.class);
}
