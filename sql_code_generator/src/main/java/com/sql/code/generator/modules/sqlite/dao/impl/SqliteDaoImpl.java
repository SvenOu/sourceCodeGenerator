package com.sql.code.generator.modules.sqlite.dao.impl;

import com.sql.code.generator.modules.sqlite.dao.SqliteDao;
import com.sql.code.generator.modules.sqlite.vo.ColumnInfo;
import com.sql.code.generator.modules.sqlite.vo.SqlliteMaster;
import com.sql.code.generator.modules.common.dao.DyDao;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class SqliteDaoImpl extends DyDao implements SqliteDao{

    private static final String SQL_FIND_ALL_TABLE = "SELECT * FROM sqlite_master WHERE type = 'table'";
    @Override
    public List<SqlliteMaster> findAllTable() {
        return queryForList(SqlliteMaster.class, SQL_FIND_ALL_TABLE);
    }

    private static final String SQL_FIND_COLUMN_INFO_BY_NAME = "PRAGMA table_info(%s)";
    @Override
    public List<ColumnInfo> findColumnsByName(String tableName) {
        String sql = String.format(SQL_FIND_COLUMN_INFO_BY_NAME, tableName);
        return queryForList(ColumnInfo.class, sql);
    }
}
