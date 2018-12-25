package com.sql.code.generator.modules.android.dao;

import com.sql.code.generator.modules.android.vo.ColumnInfo;
import com.sql.code.generator.modules.android.vo.SqlliteMaster;

import java.util.List;

public interface SqliteDao {
    List<SqlliteMaster> findAllTable();
    List<ColumnInfo> findColumnsByName(String tableName);
}
