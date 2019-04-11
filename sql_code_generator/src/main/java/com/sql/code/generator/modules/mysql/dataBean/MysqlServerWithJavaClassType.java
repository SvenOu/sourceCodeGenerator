package com.sql.code.generator.modules.mysql.dataBean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by sven-ou on 2017/2/16.
 */
public enum MysqlServerWithJavaClassType {
    BIT("bit", String.class), TINYINT("tinyint", Integer.class), BOOL("bool", Boolean.class), BOOLEAN("boolean", Boolean.class),
    SMALLINT("smallint", Integer.class), MEDIUMINT("mediumint", Integer.class), INT("int", Integer.class), INTEGER("int", Integer.class),
    BIGINT("bigint", BigInteger.class), FLOAT("float", Float.class), DOUBLE("double", Double.class),

    DECIMAL("decimal", BigDecimal.class), DATE("date", Date.class), DATETIME("datetime", Date.class), TIMESTAMP("timestamp", Date.class),
    TIME("time", Time.class), YEAR("year", Date.class), CHAR("char", String.class),
    VARCHAR("varchar", String.class), BINARY("binary", Byte[].class),

    VARBINARY("varbinary", Byte[].class), TINYBLOB("tinyblob", Byte[].class), TINYTEXT("tinytext", String.class),

    BLOB("blob", Byte[].class), TEXT("text", String.class), MEDIUMBLOB("mediumblob", Byte[].class),
    MEDIUMTEXT("mediumtext", String.class), LONGBLOB("longblob", Byte[].class),

    LONGTEXT("longtext", String.class), ENUM("enum", String.class), SET("set", String.class);

    private String sqlType;
    private Class javaClass;

    private MysqlServerWithJavaClassType(String sqliteType, Class javaClass) {
        this.sqlType = sqliteType;
        this.javaClass = javaClass;
    }

    public static Class getJavaTypeClass(String sqliteType) {
        for (MysqlServerWithJavaClassType c : MysqlServerWithJavaClassType.values()) {
            if (c.getSqliteType().equalsIgnoreCase(sqliteType.replaceAll("\\s*[(].*?[)].*", ""))) {
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
