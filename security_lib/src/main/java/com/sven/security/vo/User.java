package com.sven.security.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.lang.Override;
import java.lang.String;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
  private String userId;
  private String password;
  private String userAlias;
  private String roles;

  /**
   *  0 或者 1
   */
  private int active;
  
  public void setUserId(String userId) {
    this.userId = userId;
  }
  public String getUserId() {
    return this.userId;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getPassword() {
    return this.password;
  }
  public void setUserAlias(String userAlias) {
    this.userAlias = userAlias;
  }
  public String getUserAlias() {
    return this.userAlias;
  }
  public void setRoles(String roles) {
    this.roles = roles;
  }
  public String getRoles() {
    return this.roles;
  }
  public void setActive(int active) {
    this.active = active;
  }
  public int getActive() {
    return this.active;
  }
  
  @Override
  public String toString() {
    return String.format(
    "User{userId='%s',password='%s',userAlias='%s',roles='%s',active='%s' }",
        userId,password,userAlias,roles,active);
  }
}
