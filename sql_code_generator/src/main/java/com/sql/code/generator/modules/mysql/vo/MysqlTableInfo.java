package com.sql.code.generator.modules.mysql.vo;

import java.util.Date;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class MysqlTableInfo {
    private String tableName;
    private Date createTime;
    private Date updateTime;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
