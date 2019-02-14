package com.sql.code.generator.modules.common.vo;

public class JsonDataSource {
    private String dataSourceId;
    private String type;
    private String dataSourceName;
    private String jsonData;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "JsonDataSource{" +
                "dataSourceId='" + dataSourceId + '\'' +
                ", type='" + type + '\'' +
                ", dataSourceName='" + dataSourceName + '\'' +
                ", jsonData='" + jsonData + '\'' +
                '}';
    }
}
