package com.sql.code.generator.modules.common.dao.impl;

import com.sql.code.generator.modules.common.dao.CodeTemplateDao;
import com.sql.code.generator.modules.common.vo.CodeTemplate;
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
public class CodeTemplateDaoImpl extends NamedParameterJdbcDaoSupport implements CodeTemplateDao {
  private static final Log log = LogFactory.getLog(CodeTemplateDaoImpl.class);

  private static final String CODE_TEMPLATE_TABLE_NAME = "code_template";

  private static final RowMapper<CodeTemplate> CODE_TEMPLATE_ROW_MAPPER = BeanPropertyRowMapper.newInstance(CodeTemplate.class);

  private final Map<String, String> sqlCache = new ConcurrentHashMap<>();

  @Autowired
  @Qualifier("sqliteDataSource")
  private javax.sql.DataSource dataSource;

  @PostConstruct
  private void initialize() {
    setDataSource(dataSource);
  }

  private static final String SQL_INSERT = "INSERT INTO code_template (template_id, path, lock, owner) " +
          " VALUES (:templateId, :path, :lock, :owner)";
  @Override
  public int insert(CodeTemplate codeTemplate) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_INSERT, new BeanPropertySqlParameterSource(codeTemplate));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }

  private static final String SQL_FIND_BY_KEY = "SELECT template_id, path, lock, owner FROM code_template " +
          " WHERE template_id = :templateId";
  @Override
  public CodeTemplate findByKey(String templateId) {
    try {
      return this.getJdbcTemplate().queryForObject(SQL_FIND_BY_KEY, CODE_TEMPLATE_ROW_MAPPER, templateId);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public List<CodeTemplate> findListByKey(String templateId) {
    try {
      return this.getJdbcTemplate().query(SQL_FIND_BY_KEY, CODE_TEMPLATE_ROW_MAPPER, templateId);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return null;
    }
  }

  private static final String SQL_UPDATE = "UPDATE code_template SET template_id = :templateId, path = :path, lock = :lock, owner = :owner " +
          " WHERE template_id = :templateId";
  @Override
  public int update(CodeTemplate codeTemplate) {
    try {
      return this.getNamedParameterJdbcTemplate().update(SQL_UPDATE, new BeanPropertySqlParameterSource(codeTemplate));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
  private static final String SQL_KEY_PREFIX_UPDATE = "update|";
  private static final String SQL_UPDATE_FIELDS_TEMPLE = "UPDATE code_template SET %s " +
          " WHERE template_id = :templateId";
  @Override
  public int updateFields(CodeTemplate codeTemplate, String[] fields) {
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
      result = this.getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(codeTemplate));
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
    }
    return result;
  }

  private String getKeyByFields(String prefix, String[] fields) {
    StringBuilder sb = new StringBuilder(prefix);
    for(String templateId : fields) {
      if(!StringUtils.isEmpty(templateId)) {
        sb.append("+");
      }
      sb.append(templateId);
    }
    return sb.toString();
  }

  @Override
  public int save(CodeTemplate codeTemplate) {
    int result = -1;
    if(null != findByKey(codeTemplate.getTemplateId())) {
      result = update(codeTemplate);
    }
    else {
      result = insert(codeTemplate);
    }
    return result;
  }

  private static final String SQL_DELETE = "DELETE FROM code_template " +
          " WHERE template_id = :templateId";
  @Override
  public int deleteByKey(String templateId) {
    try {
      return this.getJdbcTemplate().update(SQL_DELETE, templateId);
    }
    catch(Exception e) {
      log.error("Error : " + e.getMessage());
      return -1;
    }
  }
}
