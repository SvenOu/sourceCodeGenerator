package ${{voPackageName}};

import java.io.Serializable;
import java.lang.Override;
$tp-repeat(fielsImport){{import $(name);
}}

public class ${{voClassName}} implements Serializable {
  private static final long serialVersionUID = 1L;

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
