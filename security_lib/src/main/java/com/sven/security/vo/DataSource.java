package com.sven.security.vo;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;


public class DataSource implements Serializable {
  private static final long serialVersionUID = 1L;

  private String dataSourceId;
  private String type;
  private String url;
  private String userName;
  private String password;
  
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
  
  @Override
  public String toString() {
    return String.format(
    "DataSource{dataSourceId='%s',type='%s',url='%s',userName='%s',password='%s' }",
        dataSourceId,type,url,userName,password);
  }
}
