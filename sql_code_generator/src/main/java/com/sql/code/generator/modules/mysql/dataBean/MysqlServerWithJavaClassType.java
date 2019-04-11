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
    BIT("bit", String.class), TINYINT("tinyint", int.class), BOOL("bool", boolean.class), BOOLEAN("boolean", boolean.class),
    SMALLINT("smallint", int.class), MEDIUMINT("mediumint", int.class), INT("int", int.class), INTEGER("int", int.class),
    BIGINT("bigint", BigInteger.class), FLOAT("float", Float.class), DOUBLE("double", Double.class),

    DECIMAL("decimal", BigDecimal.class), DATE("date", Date.class), DATETIME("datetime", Date.class), TIMESTAMP("timestamp", Date.class),
    TIME("time", Time.class), YEAR("year", Date.class), CHAR("char", String.class),
    VARCHAR("varchar", String.class), BINARY("binary", byte[].class),

    VARBINARY("varbinary", byte[].class), TINYBLOB("tinyblob", byte[].class), TINYTEXT("tinytext", String.class),

    BLOB("blob", byte[].class), TEXT("text", String.class), MEDIUMBLOB("mediumblob", byte[].class),
    MEDIUMTEXT("mediumtext", String.class), LONGBLOB("longblob", byte[].class),

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
