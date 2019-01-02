package com.sven.security.dao;

import com.sven.security.vo.DataSource;

import java.lang.Object;
import java.lang.String;
import java.util.List;
import java.util.Map;

public interface DataSourceDao {
  int insert(DataSource dataSource);

  DataSource findByKey(String key);

  List<DataSource> findListByKey(String dataSourceId);

  int update(DataSource dataSource);

  int updateFields(DataSource dataSource, String[] fields);

  int save(DataSource dataSource);

  int deleteByKey(String dataSourceId);
}
