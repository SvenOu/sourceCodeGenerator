package com.sql.code.generator.modules.common.bean;

import java.util.Arrays;
import java.util.List;

public enum DatasourceEnum {
    /**
     *
     */
    MSSQL("mssql", "net.sourceforge.jtds.jdbc.Driver", "jdbc:jtds:sqlserver://"),
    SQLITE("sqlite", "org.sqlite.JDBC", "jdbc:sqlite:");
    private String value;
    private String driveClass;
    private String urlPrefix;

    DatasourceEnum(String value, String driveClass, String urlPrefix) {
        this.value = value;
        this.driveClass = driveClass;
        this.urlPrefix = urlPrefix;
    }

    public static List<DatasourceEnum> getAll(){
        return Arrays.asList(DatasourceEnum.class.getEnumConstants());
    }
    public static String[] getAllValues(){
        List<DatasourceEnum> roleEnums = getAll();
        String[] vals = new String[roleEnums.size()];
        for(int i = 0; i<roleEnums.size(); i++){
            vals[i] = roleEnums.get(i).getValue();
        }
        return vals;
    }

    public String getValue() {
        return value;
    }

    public String getDriveClass() {
        return driveClass;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }
}