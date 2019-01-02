package com.sql.code.generator.modules.mssql.vo;

import java.io.Serializable;
import java.lang.Override;
import java.util.Date;
import java.lang.String;


public class PubScenario implements Serializable {
  private static final long serialVersionUID = 1L;

  private String scenarioId;
  private String bmu;
  private String picPath;
  private String desc;
  private String answer;
  private String value0;
  private String value1;
  private String value2;
  private String versionId;
  private String publishedBy;
  private Date publishDate;
  private String modifyBy;
  private Date modifyDate;
  private short displayOrder;
  private boolean locked;
  private Date startDate;
  private Date endDate;
  private boolean refRvt;
  private boolean refRvtIntl;
  private String recommendedReason;
  
  public void setScenarioId(String scenarioId) {
    this.scenarioId = scenarioId;
  }
  public String getScenarioId() {
    return this.scenarioId;
  }
  public void setBmu(String bmu) {
    this.bmu = bmu;
  }
  public String getBmu() {
    return this.bmu;
  }
  public void setPicPath(String picPath) {
    this.picPath = picPath;
  }
  public String getPicPath() {
    return this.picPath;
  }
  public void setDesc(String desc) {
    this.desc = desc;
  }
  public String getDesc() {
    return this.desc;
  }
  public void setAnswer(String answer) {
    this.answer = answer;
  }
  public String getAnswer() {
    return this.answer;
  }
  public void setValue0(String value0) {
    this.value0 = value0;
  }
  public String getValue0() {
    return this.value0;
  }
  public void setValue1(String value1) {
    this.value1 = value1;
  }
  public String getValue1() {
    return this.value1;
  }
  public void setValue2(String value2) {
    this.value2 = value2;
  }
  public String getValue2() {
    return this.value2;
  }
  public void setVersionId(String versionId) {
    this.versionId = versionId;
  }
  public String getVersionId() {
    return this.versionId;
  }
  public void setPublishedBy(String publishedBy) {
    this.publishedBy = publishedBy;
  }
  public String getPublishedBy() {
    return this.publishedBy;
  }
  public void setPublishDate(Date publishDate) {
    this.publishDate = publishDate;
  }
  public Date getPublishDate() {
    return this.publishDate;
  }
  public void setModifyBy(String modifyBy) {
    this.modifyBy = modifyBy;
  }
  public String getModifyBy() {
    return this.modifyBy;
  }
  public void setModifyDate(Date modifyDate) {
    this.modifyDate = modifyDate;
  }
  public Date getModifyDate() {
    return this.modifyDate;
  }
  public void setDisplayOrder(short displayOrder) {
    this.displayOrder = displayOrder;
  }
  public short getDisplayOrder() {
    return this.displayOrder;
  }
  public void setLocked(boolean locked) {
    this.locked = locked;
  }
  public boolean getLocked() {
    return this.locked;
  }
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
  public Date getStartDate() {
    return this.startDate;
  }
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }
  public Date getEndDate() {
    return this.endDate;
  }
  public void setRefRvt(boolean refRvt) {
    this.refRvt = refRvt;
  }
  public boolean getRefRvt() {
    return this.refRvt;
  }
  public void setRefRvtIntl(boolean refRvtIntl) {
    this.refRvtIntl = refRvtIntl;
  }
  public boolean getRefRvtIntl() {
    return this.refRvtIntl;
  }
  public void setRecommendedReason(String recommendedReason) {
    this.recommendedReason = recommendedReason;
  }
  public String getRecommendedReason() {
    return this.recommendedReason;
  }
  
  @Override
  public String toString() {
    return String.format(
    "PubScenario{scenarioId='%s',bmu='%s',picPath='%s',desc='%s',answer='%s',value0='%s',value1='%s',value2='%s',versionId='%s',publishedBy='%s',publishDate='%s',modifyBy='%s',modifyDate='%s',displayOrder='%s',locked='%s',startDate='%s',endDate='%s',refRvt='%s',refRvtIntl='%s',recommendedReason='%s' }",
        scenarioId,bmu,picPath,desc,answer,value0,value1,value2,versionId,publishedBy,publishDate,modifyBy,modifyDate,displayOrder,locked,startDate,endDate,refRvt,refRvtIntl,recommendedReason);
  }
}
