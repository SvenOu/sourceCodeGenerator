package com.sql.code.generator.modules.sqlite.dao;

import com.sql.code.generator.modules.sqlite.vo.ColumnInfo;
import com.sql.code.generator.modules.sqlite.vo.SqlliteMaster;

import java.util.List;

public interface SqliteDao {
    List<SqlliteMaster> findAllTable();
    List<ColumnInfo> findColumnsByName(String tableName);
}
