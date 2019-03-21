package ${{daoPackageName}};

import java.lang.Object;
import java.lang.String;
import java.util.List;
import java.util.Map;

public interface ${{daoClassName}} {
  int insert(${{voClassName}} ${{voSqlName-underlineToCame}});

  ${{voClassName}} findByKey(String key);

  int update(${{voClassName}} ${{voSqlName-underlineToCame}});

  int updateFields(${{voClassName}} ${{voSqlName-underlineToCame}}, Map<String, Object> values);

  default int save(${{voClassName}} ${{voSqlName-underlineToCame}}) {
    int result = -1;
    if(null != findByKey(${{voSqlName-underlineToCame}}.getKey())) {
        result = update(${{voSqlName-underlineToCame}});
    }
    else {
        result = insert(${{voSqlName-underlineToCame}});
    }
    return result;
  }
  int deleteByKey(String key);
}
