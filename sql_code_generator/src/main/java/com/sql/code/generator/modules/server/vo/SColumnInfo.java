package com.sql.code.generator.modules.server.vo;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class SColumnInfo {
    private String tableName;
    private String columnName;
    private String type;
    private int maxLength;

    /**
     *
     * FIXME: 用于过滤column的特殊的标点符号(转换为"_")，需要使用者自行维护
     * 用“|”分隔每个特殊符号
     */
    private static final String specialPunctuationRegex = "\\s|\\#";

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName.replaceAll(specialPunctuationRegex, "_");
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
