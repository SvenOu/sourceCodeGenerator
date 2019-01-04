package com.sql.code.generator.modules.common.dao;


import com.sql.code.generator.modules.common.vo.DataSource;

import java.util.List;

public interface DataSourceDao {
  int insert(DataSource dataSource);

  DataSource findByKey(String dataSourceId);

  List<DataSource> findAll();

  List<DataSource> findListByKey(String dataSourceId);

  int update(DataSource dataSource);

  int updateFields(DataSource dataSource, String[] fields);

  int save(DataSource dataSource);

  int deleteByKey(String dataSourceId);
}
