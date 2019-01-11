package com.sql.code.generator.modules.common.vo;

import java.io.Serializable;


public class DataSource implements Serializable {
    private static final long serialVersionUID = 1L;

    private String dataSourceId;
    private String type;
    private String url;
    private String userName;
    private String password;
    private String driveClass;
    private boolean lock;
    private String owner;
    private String jsonData;

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceId() {
        return this.dataSourceId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public boolean getLock() {
        return this.lock;
    }

    public String getDriveClass() {
        return driveClass;
    }

    public void setDriveClass(String driveClass) {
        this.driveClass = driveClass;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "DataSource{" +
                "dataSourceId='" + dataSourceId + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", driveClass='" + driveClass + '\'' +
                ", lock=" + lock +
                ", owner='" + owner + '\'' +
                ", jsonData='" + jsonData + '\'' +
                '}';
    }
}
