package com.sql.code.generator.modules.server.dataBean;

import net.sourceforge.jtds.jdbc.DateTime;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sven-ou on 2017/2/16.
 */
public enum SSQLServerWithJavaClassType {
    BIGINT("bigint", long.class), BINARY("binary", byte[].class), BIT("bit", boolean.class),
    CHAR("char", String.class), DATE("date", Date.class), TIMESTAMP("datetime", Date.class),
    TIMESTAMP2("datetime2", Date.class), DATETIMEOFFSET("datetimeoffset", DateTime.class), DECIMAL("decimal", BigDecimal.class),

    DOUBLE("float", double.class), LONGVARBINARY("image", byte[].class), INTEGER("int", int.class),
    DECIMAL2("money", BigDecimal.class), CHAR2("nchar", String.class), LONGVARCHAR("ntext", String.class),
    NUMERIC("numeric", BigDecimal.class), VARCHAR("nvarchar", String.class),

    VARCHAR2("nvarchar", String.class), REAL("real", float.class), TIMESTAMP3("smalldatetime", Date.class),

    SMALLINT("smallint", short.class), DECIMAL3("smallmoney", BigDecimal.class), LONGVARCHAR2("text", String.class),
    TIME("time", Date.class), SYSNAME("sysname", String.class),

    BINARY2("timestamp", byte[].class), TINYINT("tinyint", short.class), VARBINARY2("udt", byte[].class),
    CHAR3("uniqueidentifier", String.class), VARBINARY3("varbinary", byte[].class), VARBINARY("varbinary", byte[].class),
    VARCHAR3("varchar", String.class), LONGVARCHAR3("xml", String.class);

    private String sqlType;
    private Class javaClass;

    private SSQLServerWithJavaClassType(String sqliteType, Class javaClass) {
        this.sqlType = sqliteType;
        this.javaClass = javaClass;
    }

    public static Class getJavaTypeClass(String sqliteType) {
        for (SSQLServerWithJavaClassType c : SSQLServerWithJavaClassType.values()) {
            if (c.getSqliteType().equalsIgnoreCase(sqliteType.replaceAll("[(].*?[)]", ""))) {
                return c.getJavaClass();
            }
        }
        return null;
    }

    public String getSqliteType() {
        return sqlType;
    }

    public void setSqliteType(String sqliteType) {
        this.sqlType = sqliteType;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public void setJavaClass(Class javaClass) {
        this.javaClass = javaClass;
    }
}
