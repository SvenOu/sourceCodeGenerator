package com.sql.code.generator.modules.mysql.dao.impl;

import com.sql.code.generator.modules.common.dao.DyDao;
import com.sql.code.generator.modules.mysql.dao.MysqlTableInfoDAO;
import com.sql.code.generator.modules.mysql.vo.MysqlColumnInfo;
import com.sql.code.generator.modules.mysql.vo.MysqlTableInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MysqlDaoImpl extends DyDao implements MysqlTableInfoDAO {

    private static final String SQL_FIND_ALL_TABLE = "SELECT * FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s'";
    @Override
    public List<MysqlTableInfo> findAllTable() {
        return queryForList(MysqlTableInfo.class, String.format(SQL_FIND_ALL_TABLE, dataBaseName));
    }

    private static final String SQL_FIND_COLUMN_INFO_BY_NAME = "select o.TABLE_NAME as table_name, o.COLUMN_NAME as column_name, o.COLUMN_TYPE as type, o.CHARACTER_MAXIMUM_LENGTH as max_length" +
            " FROM INFORMATION_SCHEMA.COLUMNS o" +
            " WHERE TABLE_SCHEMA = '%s' AND TABLE_NAME = '%s'";

    @Override
    public List<MysqlColumnInfo> findColumnsByName(String tableName) {
        String sql = String.format(SQL_FIND_COLUMN_INFO_BY_NAME, dataBaseName, tableName);
        return queryForList(MysqlColumnInfo.class, sql);
    }
}
