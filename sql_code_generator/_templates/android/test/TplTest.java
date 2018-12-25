package ${{testPackageName}};

import ${{voPackageName}}.${{voClassName}};

public class ${{testClassName}} {
  public void test() {
    ${{voClassName}} ${{voSqlName-underlineToCame}} = new ${{voClassName}};
    $tp-repeat(fields){{${{voSqlName-underlineToCame}}.set$(name-underlineToCameUPCASEFirst)("$(name-underlineToCame)");
    }}
  }
}
