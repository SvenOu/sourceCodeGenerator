package ${{daoPackageName}};

import java.lang.Object;
import java.lang.String;
import java.util.List;
import java.util.Map;
import ${{voPackageName}}.${{voClassName}};

public interface ${{daoClassName}} {
  int insert(${{voClassName}} ${{voSqlName-underlineToCame}});

  ${{voClassName}} findByKey(String key);

  List<${{voClassName}}> findListByKey(String key);

  int update(${{voClassName}} ${{voSqlName-underlineToCame}});

  int updateFields(${{voClassName}} ${{voSqlName-underlineToCame}}, Map<String, Object> values);

  int updateField(String key, String field, Object value);

  int save(${{voClassName}} ${{voSqlName-underlineToCame}});

  int deleteByKey(String key);
}
