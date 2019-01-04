package com.sven.security.dao.impl;


import com.sven.security.dao.UserDao;
import com.sven.security.utils.TextUtils;
import com.sven.security.vo.User;
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
import javax.sql.DataSource;
import java.util.Arrays;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Repository
public class UserDaoImpl extends NamedParameterJdbcDaoSupport implements UserDao {
  private static final Log log = LogFactory.getLog(UserDaoImpl.class);

  private static final String USER_TABLE_NAME = "user";

  private static final RowMapper<User> USER_ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

  private final Map<String, String> sqlCache = new ConcurrentHashMap<>();

  private static final String SQL_INSERT = "INSERT INTO user (user_id, password, user_alias, roles, active) " +
          " VALUES (:userId, :password, :userAlias, :roles, :active)";

  @Autowired
  @Qualifier("securitysqliteDataSource")
  private DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  @Override
  public int insert(User user) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_INSERT, new BeanPropertySqlParameterSource(user));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }

  private static final String SQL_FIND_BY_KEY = "SELECT user_id, password, user_alias, roles, active FROM user " +
          " WHERE user_id = :userId";
  @Override
  public User findByKey(String key) {
    try {
      return this.getJdbcTemplate().queryForObject(SQL_FIND_BY_KEY, USER_ROW_MAPPER, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<User> findListByKey(String key) {
    try {
      return this.getJdbcTemplate().query(SQL_FIND_BY_KEY, USER_ROW_MAPPER, key);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  private static final String SQL_UPDATE = "UPDATE user SET user_id = :userId, password = :password, user_alias = :userAlias, roles = :roles, active = :active " +
          " WHERE user_id = :userId";
  @Override
  public int update(User user) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_UPDATE, new BeanPropertySqlParameterSource(user));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
  private static final String SQL_KEY_PREFIX_UPDATE = "update|";
  private static final String SQL_UPDATE_FIELDS_TEMPLE = "UPDATE user SET %s " +
          " WHERE user_id = :userId";
  @Override
  public int updateFields(User user, String[] fields) {
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
      result = this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(user));
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
  public int save(User user) {
    int result = -1;
    if(null != findByKey(user.getUserId())) {
      result = update(user);
    }
    else {
      result = insert(user);
    }
    return result;
  }

  private static final String SQL_DELETE = "DELETE FROM user " +
          " WHERE user_id = :userId";
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
