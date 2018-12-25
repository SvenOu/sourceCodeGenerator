package com.sql.code.generator.modules.android.vo;

/**
 * Created by sven-ou on 2017/2/15.
 */
public class ColumnInfo {
    private Integer cid;
    private String name;
    private String type;
    private Boolean notnull;
    private String dfltValue;
    private Boolean pk;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getNotnull() {
        return notnull;
    }

    public void setNotnull(Boolean notnull) {
        this.notnull = notnull;
    }

    public String getDfltValue() {
        return dfltValue;
    }

    public void setDfltValue(String dfltValue) {
        this.dfltValue = dfltValue;
    }

    public Boolean getPk() {
        return pk;
    }

    public void setPk(Boolean pk) {
        this.pk = pk;
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", notnull=" + notnull +
                ", dfltValue='" + dfltValue + '\'' +
                ", pk=" + pk +
                '}';
    }
}
