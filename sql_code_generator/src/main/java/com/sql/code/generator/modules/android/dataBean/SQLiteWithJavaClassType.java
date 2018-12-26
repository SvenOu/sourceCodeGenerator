package com.sql.code.generator.modules.android.dataBean;

import java.sql.Clob;
import java.util.Date;

/**
 * Created by sven-ou on 2017/2/16.
 */
public enum SQLiteWithJavaClassType {
    INT("INT", int.class), INTEGER("INTEGER", int.class), TINYINT("TINYINT", byte.class),
    SMALLINT("SMALLINT", short.class), MEDIUMINT("MEDIUMINT", int.class), BIGINT("BIGINT", long.class),
    UNSIGNED_BIG_INT("UNSIGNED BIG INT", long.class), INT2("INT2", int.class), INT8("INT8", int.class),

    CHARACTER("CHARACTER", String.class), VARCHAR("VARCHAR", String.class), VARYING_CHARACTER("VARYING CHARACTER", String.class),
    NCHAR("NCHAR", String.class), NATIVE_CHARACTER("NATIVE CHARACTER", String.class), NVARCHAR("NVARCHAR", String.class),
    TEXT("TEXT", String.class), CLOB("CLOB", Clob.class),

    BLOB("BLOB", byte[].class), NO_DATATYPE_SPECIFIED("no datatype specified", Object.class), EMPTY("", Object.class),

    REAL("REAL", float.class), DOUBLE("DOUBLE", double.class), DOUBLE_PRECISION("DOUBLE PRECISION", double.class),
    FLOAT("FLOAT", float.class),

    NUMERIC("NUMERIC", String.class), DECIMAL("DECIMAL", double.class), BOOL("BOOL", boolean.class),
    BOOLEAN("BOOLEAN", boolean.class), DATE("DATE", Date.class), DATETIME("DATETIME", Date.class);

    private String sqlType;
    private Class javaClass;

    private SQLiteWithJavaClassType(String sqliteType, Class javaClass) {
        this.sqlType = sqliteType;
        this.javaClass = javaClass;
    }

    public static Class getJavaTypeClass(String sqliteType) {
        for (SQLiteWithJavaClassType c : SQLiteWithJavaClassType.values()) {
            if (c.getSqliteType().equalsIgnoreCase(sqliteType.replaceFirst("\\s*[(].*?[)]", ""))) {
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
