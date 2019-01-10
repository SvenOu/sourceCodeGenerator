package ${{daoImplPackageName}};

import ${{daoPackageName}}.${{daoClassName}};
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.util.StringUtils;
import ${{voPackageName}}.${{voClassName}};

@Repository
public class ${{daoImplClassName}} extends NamedParameterJdbcDaoSupport implements ${{daoClassName}} {
  private static final Log log = LogFactory.getLog(${{daoImplClassName}}.class);

  private static final String ${{voSqlName-upCaseALL}}_TABLE_NAME = "${{voSqlName}}";

  private static final RowMapper<${{voClassName}}> ${{voSqlName-upCaseALL}}_ROW_MAPPER = BeanPropertyRowMapper.newInstance(${{voClassName}}.class);

  private final Map<String, String> sqlCache = new ConcurrentHashMap<>();

  @Autowired
  @Qualifier("sqliteDataSource")
  private javax.sql.DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  private static final String SQL_INSERT = "INSERT INTO ${{voSqlName}} ($tp-repeat(sqlFields-suffixNotIncludeEnd~, ){{$(name)}}) " +
          " VALUES ($tp-repeat(sqlFields-suffixNotIncludeEnd~, ){{:$(name-underlineToCame)}})";
  @Override
  public int insert(${{voClassName}} ${{voSqlName-underlineToCame}}) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_INSERT, new BeanPropertySqlParameterSource(${{voSqlName-underlineToCame}}));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }

  private static final String SQL_FIND_BY_KEY = "SELECT $tp-repeat(sqlFields-suffixNotIncludeEnd~, ){{$(name)}} FROM ${{voSqlName}} " +
          " WHERE key = :key";
  @Override
  public ${{voClassName}} findByKey(String key) {
    try {
      return this.getJdbcTemplate().queryForObject(SQL_FIND_BY_KEY, ${{voSqlName-upCaseALL}}_ROW_MAPPER, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<${{voClassName}}> findListByKey(String key) {
    try {
      return this.getJdbcTemplate().query(SQL_FIND_BY_KEY, ${{voSqlName-upCaseALL}}_ROW_MAPPER, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  private static final String SQL_UPDATE = "UPDATE ${{voSqlName}} SET $tp-repeat(sqlFields-suffixNotIncludeEnd~, ){{$(name) = :$(name-underlineToCame)}} " +
          " WHERE key = :key";
  @Override
  public int update(${{voClassName}} ${{voSqlName-underlineToCame}}) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_UPDATE, new BeanPropertySqlParameterSource(${{voSqlName-underlineToCame}}));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
  private static final String SQL_KEY_PREFIX_UPDATE = "update|";
  private static final String SQL_UPDATE_FIELDS_TEMPLE = "UPDATE ${{voSqlName}} SET %s " +
          " WHERE key = :key";
  @Override
  public int updateFields(${{voClassName}} ${{voSqlName-underlineToCame}}, String[] fields) {
    int result = -1;
    if(fields.length == 0) {
      return result;
    }
    Arrays.sort(fields);
    String sqlKey = this.getKeyByFields(SQL_KEY_PREFIX_UPDATE, fields);
    String sql = this.sqlCache.get(sqlKey);
    if (null == sql) {
      StringBuilder sqlBlock = new StringBuilder();
      for (int i = 0, len = fields.length; i < len; i++) {
        if(i >= 1) {
          sqlBlock.append(", ");
        }
        String columnName = TextUtils.underscoreName(fields[i]);
        String propertyName = TextUtils.camelCaseName(fields[i]);
        sqlBlock.append(columnName).append(" = ").append(":").append(propertyName);
      }
      sql = String.format(SQL_UPDATE_FIELDS_TEMPLE, sqlBlock);
      sqlCache.put(sqlKey, sql);
    }
    try {
      result = this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(${{voSqlName-underlineToCame}}));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
    }
    return result;
  }

  private String getKeyByFields(String prefix, String[] fields) {
    StringBuilder sb = new StringBuilder(prefix);
    for(String key : fields) {
      if(!StringUtils.isEmpty(key)) {
        sb.append("+");
      }
      sb.append(key);
    }
    return sb.toString();
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

  private static final String SQL_DELETE = "DELETE FROM ${{voSqlName}} " +
          " WHERE key = :key";
  @Override
  public int deleteByKey(String key) {
    try {
      return this.getJdbcTemplate().update(SQL_DELETE, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
}
