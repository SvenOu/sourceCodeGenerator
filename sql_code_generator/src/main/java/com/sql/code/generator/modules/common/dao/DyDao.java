package com.sql.code.generator.modules.common.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sven-ou
 */
public class DyDao {
    private static Log log = LogFactory.getLog(DyDao.class);

    protected String driverClassName;
    protected String url;
    protected String username;
    protected String password;
    protected String dataBaseName;

    public void configDyDao(String driverClassName, String url, String username, String password) {
        this.driverClassName = driverClassName;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void configDyDao(String driverClassName, String url, String username, String password, String dataBaseName) {
        configDyDao(driverClassName, url, username, password);
        this.dataBaseName = dataBaseName;
    }

    public <T> List<T> queryForList(Class<T> mappedClass, String sql){
        List<T> results = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName(driverClassName);
            c = DriverManager.getConnection(url, username, password);
            c.setAutoCommit(false);
            log.info("Opened database successfully");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            BeanPropertyRowMapper<T> rowerMappper = BeanPropertyRowMapper.newInstance(mappedClass);
            int i = 0;
            while (rs.next()) {
                T t = rowerMappper.mapRow(rs, i);
                results.add(t);
                i++;
            }
            rs.close();
            stmt.close();
            c.close();
            log.info("QueryForList done successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  results;
    };

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataBaseName() {
        return dataBaseName;
    }

    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }
}
