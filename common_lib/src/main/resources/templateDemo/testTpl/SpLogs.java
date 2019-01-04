package vo;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;
import java.util.Date;


${{title}}
public class SpLogs implements Serializable {

  ${{name}}
  private static final long serialVersionUID = 1L;

  $tp-repeat(fiels){{
  private $(type) $(name);
  }}
  $tp-repeat(fiels){{
  public void set$(name-upCaseFirst)($(type) $(name)) {
    this.$(name) = $(name);
  }
  }}
  private int id;

  private Date runingDate;

  private String parameter;

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }



  public void setRuningDate(Date runingDate) {
    this.runingDate = runingDate;
  }

  public Date getRuningDate() {
    return this.runingDate;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public String getParameter() {
    return this.parameter;
  }

  @Override
  public String toString() {
    return String.format("SpLogs{id='%s',runingDate='%s',parameter='%s'}",
        id,runingDate,parameter);
  }
}
