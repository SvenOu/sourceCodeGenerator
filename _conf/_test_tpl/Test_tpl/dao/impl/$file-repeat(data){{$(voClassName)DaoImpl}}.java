package ${{daoImplPackageName}};

import com.tts.android.mybatic.bean.BeanPropertyContentValues;
import com.tts.android.mybatic.bean.BeanPropertyRowMapper;
import com.tts.android.db.DaoSupport;
import com.tts.android.db.RowMapper;
import ${{daoPackageName}}.${{daoClassName}};
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ${{voPackageName}}.${{voClassName}};

public class ${{daoImplClassName}} extends DaoSupport implements ${{daoClassName}} {
  private static final String TAG = ${{daoImplClassName}}.class.getSimpleName();

  private static final String ${{voSqlName-upCaseALL}}_TABLE_NAME = "${{voSqlName}}";

  private static final BeanPropertyRowMapper<${{voClassName}}> ${{voSqlName-upCaseALL}}_ROW_MAPPER = BeanPropertyRowMapper.newInstance(${{voClassName}}.class);

  @Override
  public int insert(${{voClassName}} ${{voSqlName-underlineToCame}}) {
    long rowId = this.getDbTemplate().insert(${{voSqlName-upCaseALL}}_TABLE_NAME,
            BeanPropertyContentValues.newInstance(${{voSqlName-underlineToCame}}));
    return -1 < rowId ? 1 : 0;
  }

  private static final String SQL_FIND_BY_KEY = "SELECT $tp-repeat(sqlFields-suffixNotIncludeEnd~, ){{$(name)}} FROM ${{voSqlName}} WHERE key = ?";
  @Override
  public ${{voClassName}} findByKey(String key) {
    return this.getDbTemplate().queryForObject(SQL_FIND_BY_KEY, ${{voSqlName-upCaseALL}}_ROW_MAPPER, key);
  }

  @Override
  public List<${{voClassName}}> findListByKey(String key) {
    return this.getDbTemplate().query(SQL_FIND_BY_KEY, ${{voSqlName-upCaseALL}}_ROW_MAPPER, key);
  }

  @Override
  public int update(${{voClassName}} ${{voSqlName-underlineToCame}}) {
    return this.getDbTemplate().update(${{voSqlName-upCaseALL}}_TABLE_NAME, BeanPropertyContentValues.newInstance(${{voSqlName-underlineToCame}}),
        " key = ? ", ${{voSqlName-underlineToCame}}.getKey());
  }

  @Override
  public int updateFields(${{voClassName}} ${{voSqlName-underlineToCame}}, Map<String, Object> values) {
    return this.getDbTemplate().update(${{voSqlName-upCaseALL}}_TABLE_NAME, values, "key = ?", key);
  }

  @Override
  public int updateField(String key, String field, Object value) {
    Map<String, Object> values = new HashMap<>(1);
    values.put(field, value);
    return this.getDbTemplate().update(${{voSqlName-upCaseALL}}_TABLE_NAME, values, "key = ?", key);
  }

  @Override
  public int save(${{voClassName}} ${{voSqlName-underlineToCame}}) {
    int result = -1;
    if(null != findByKey(${{voSqlName-underlineToCame}}.getKey())) {
      result = update(${{voSqlName-underlineToCame}});
    }
    else {
      result = insert(${{voSqlName-underlineToCame}});
    }
    return result;
  }

  @Override
  public int deleteByKey(String key) {
    return this.getDbTemplate().delete(${{voSqlName-upCaseALL}}_TABLE_NAME, "key = ?", key);
  }
}
