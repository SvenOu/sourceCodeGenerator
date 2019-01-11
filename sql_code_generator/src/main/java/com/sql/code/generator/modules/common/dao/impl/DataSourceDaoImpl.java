package com.sql.code.generator.modules.common.dao.impl;

import com.sql.code.generator.modules.common.dao.DataSourceDao;
import com.sql.code.generator.modules.common.vo.DataSource;
import com.sven.common.lib.codetemplate.utils.TextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Repository
public class DataSourceDaoImpl extends NamedParameterJdbcDaoSupport implements DataSourceDao {
  private static final Log log = LogFactory.getLog(DataSourceDaoImpl.class);

  private static final String DATA_SOURCE_TABLE_NAME = "data_source";

  private static final RowMapper<DataSource> DATA_SOURCE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(DataSource.class);

  private final Map<String, String> sqlCache = new ConcurrentHashMap<>();

  @Autowired
  @Qualifier("sqliteDataSource")
  private javax.sql.DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  private static final String SQL_INSERT = "INSERT INTO data_source (data_source_id, type, url, user_name, password, lock, drive_class, owner, json_data) " +
          " VALUES (:dataSourceId, :type, :url, :userName, :password, :lock, :driveClass, :owner, :jsonData)";
  @Override
  public int insert(DataSource dataSource) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_INSERT, new BeanPropertySqlParameterSource(dataSource));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }

  private static final String SQL_FIND_BY_KEY = "SELECT data_source_id, type, url, user_name, password, lock, drive_class, owner, json_data FROM data_source " +
          " WHERE data_source_id = :dataSourceId";
  @Override
  public DataSource findByKey(String dataSourceId) {
    try {
      return this.getJdbcTemplate().queryForObject(SQL_FIND_BY_KEY, DATA_SOURCE_ROW_MAPPER, dataSourceId);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  private static final String SQL_FIND_ALL= "SELECT data_source_id, type, url, user_name, password, lock, drive_class, owner, json_data FROM data_source " +
          " WHERE owner = :owner";
  @Override
  public List<DataSource> findAll(String owner) {
    try {
      return this.getJdbcTemplate().query(SQL_FIND_ALL, DATA_SOURCE_ROW_MAPPER, owner);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<DataSource> findListByKey(String dataSourceId) {
    try {
      return this.getJdbcTemplate().query(SQL_FIND_BY_KEY, DATA_SOURCE_ROW_MAPPER, dataSourceId);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  private static final String SQL_UPDATE = "UPDATE data_source SET data_source_id = :dataSourceId, type = :type, url = :url, user_name = :userName, password = :password, lock = :lock, drive_class = :driveClass, owner = :owner, json_data = :jsonData " +
          " WHERE data_source_id = :dataSourceId";
  @Override
  public int update(DataSource dataSource) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_UPDATE, new BeanPropertySqlParameterSource(dataSource));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
  private static final String SQL_KEY_PREFIX_UPDATE = "update|";
  private static final String SQL_UPDATE_FIELDS_TEMPLE = "UPDATE data_source SET %s " +
          " WHERE data_source_id = :dataSourceId";
  @Override
  public int updateFields(DataSource dataSource, String[] fields) {
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
      result = this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(dataSource));
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
  public int save(DataSource dataSource) {
    int result = -1;
    if(null != findByKey(dataSource.getDataSourceId())) {
      result = update(dataSource);
    }
    else {
      result = insert(dataSource);
    }
    return result;
  }

  private static final String SQL_DELETE = "DELETE FROM data_source " +
          " WHERE data_source_id = :dataSourceId";
  @Override
  public int deleteByKey(String dataSourceId) {
    try {
      return this.getJdbcTemplate().update(SQL_DELETE, dataSourceId);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
}
