package com.sql.code.generator.modules.common.composite;

/**
 * @author sven-ou
 */

public enum  SqlType {
    /**
     *
     */
    SQL_SERVER_2005("sqlServer2005"), SQLLITE("sqlite");

    private String name;
    SqlType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
