package com.sven.security.dao.impl;

import com.sven.security.dao.UserRoleDao;
import com.sven.security.utils.TextUtils;
import com.sven.security.vo.UserRole;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRoleImpl extends NamedParameterJdbcDaoSupport implements UserRoleDao {
  private static final Log log = LogFactory.getLog(UserRoleImpl.class);

  private static final String USER_ROW_TABLE_NAME = "user_role";

  private static final RowMapper<UserRole> USER_ROLE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(UserRole.class);

  private final Map<String, String> sqlCache = new ConcurrentHashMap<>();

  @Autowired
  @Qualifier("securitysqliteDataSource")
  private DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  private static final String SQL_INSERT = "INSERT INTO user_role (user_role_id, role_desc) " +
          " VALUES (:userRoleId, :roleDesc)";
  @Override
  public int insert(UserRole userRole) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_INSERT, new BeanPropertySqlParameterSource(userRole));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }

  private static final String SQL_FIND_BY_KEY = "SELECT user_role_id, role_desc FROM user_role " +
          " WHERE user_role_id = :userRoleId";
  @Override
  public UserRole findByKey(String key) {
    try {
      return this.getJdbcTemplate().queryForObject(SQL_FIND_BY_KEY, USER_ROLE_ROW_MAPPER, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<UserRole> findListByKey(String key) {
    try {
      return this.getJdbcTemplate().query(SQL_FIND_BY_KEY, USER_ROLE_ROW_MAPPER, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  private static final String SQL_UPDATE = "UPDATE user_role SET user_role_id = :userRoleId, role_desc = :roleDesc" +
          " WHERE user_role_id = :userRoleId";
  @Override
  public int update(UserRole userRole) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_UPDATE, new BeanPropertySqlParameterSource(userRole));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
  private static final String SQL_KEY_PREFIX_UPDATE = "update|";
  private static final String SQL_UPDATE_FIELDS_TEMPLE = "UPDATE user_role SET %s " +
          " WHERE user_role_id = :userRoleId";
  @Override
  public int updateFields(UserRole userRole, String[] fields) {
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
      result = this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(userRole));
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
  public int save(UserRole userRole) {
    int result = -1;
    if(null != findByKey(userRole.getUserRoleId())) {
      result = update(userRole);
    }
    else {
      result = insert(userRole);
    }
    return result;
  }

  private static final String SQL_DELETE = "DELETE FROM user_role " +
          " WHERE user_role_id = :userRoleId";
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
