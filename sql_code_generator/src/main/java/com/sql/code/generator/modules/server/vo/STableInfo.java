package com.sql.code.generator.modules.server.vo;

import java.util.Date;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class STableInfo {
    private String name;
    private Date createDate;
    private Date modifyDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
