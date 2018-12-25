package ${{voPackageName}};

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.lang.Override;
$tp-repeat(fielsImport){{import $(name);
}}

@JsonIgnoreProperties(ignoreUnknown = true)
public class ${{voClassName}} implements Serializable {

  $tp-repeat(sqlFields){{public static final String $(name-upCaseALL) = "$(name)";
  }}
  $tp-repeat(fields){{private $(type) $(name-underlineToCame);
  }}
  $tp-repeat(fields){{public void set$(name-underlineToCameUPCASEFirst)($(type) $(name-underlineToCame)) {
    this.$(name-underlineToCame) = $(name-underlineToCame);
  }
  public $(type) get$(name-underlineToCameUPCASEFirst)() {
    return this.$(name-underlineToCame);
  }
  }}
  @Override
  public String toString() {
    return String.format(
    "${{voClassName}}{$tp-repeat(fields-suffixNotIncludeEnd~,){{$(name-underlineToCame)='%s'}} }",
        $tp-repeat(fields-suffixNotIncludeEnd~,){{$(name-underlineToCame)}});
  }
}
