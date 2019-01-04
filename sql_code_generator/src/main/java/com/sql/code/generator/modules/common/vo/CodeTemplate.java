package com.sql.code.generator.modules.common.vo;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;


public class CodeTemplate implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String TEMPLATE_PREFIX = "codeTemplate";
  private String templateId;
  private String path;
  private boolean lock;
  private String owner;
  
  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }
  public String getTemplateId() {
    return this.templateId;
  }
  public void setPath(String path) {
    this.path = path;
  }
  public String getPath() {
    return this.path;
  }
  public void setLock(boolean lock) {
    this.lock = lock;
  }
  public boolean getLock() {
    return this.lock;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "CodeTemplate{" +
            "templateId='" + templateId + '\'' +
            ", path='" + path + '\'' +
            ", lock=" + lock +
            ", owner='" + owner + '\'' +
            '}';
  }
}
