package com.sven.security.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.lang.Override;
import java.lang.String;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRole implements Serializable {
  private String userRoleId;
  private String roleDesc;
  
  public void setUserRoleId(String userRoleId) {
    this.userRoleId = userRoleId;
  }
  public String getUserRoleId() {
    return this.userRoleId;
  }
  public void setRoleDesc(String roleDesc) {
    this.roleDesc = roleDesc;
  }
  public String getRoleDesc() {
    return this.roleDesc;
  }
  
  @Override
  public String toString() {
    return String.format(
    "UserRole{userRoleId='%s',roleDesc='%s' }",
        userRoleId,roleDesc);
  }
}
