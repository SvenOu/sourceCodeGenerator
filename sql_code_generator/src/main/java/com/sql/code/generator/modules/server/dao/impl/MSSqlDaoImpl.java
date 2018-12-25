package com.sql.code.generator.modules.server.dao.impl;

import com.sql.code.generator.modules.common.dao.DyDao;
import com.sql.code.generator.modules.server.dao.STableInfoDAO;
import com.sql.code.generator.modules.server.vo.SColumnInfo;
import com.sql.code.generator.modules.server.vo.STableInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MSSqlDaoImpl extends DyDao implements STableInfoDAO {

    private static final String SQL_FIND_ALL_TABLE = "SELECT * FROM sys.objects where type = 'U'";
    @Override
    public List<STableInfo> findAllTable() {
        return queryForList(STableInfo.class, SQL_FIND_ALL_TABLE);
    }

    private static final String SQL_FIND_COLUMN_INFO_BY_NAME = "select o.name as table_name, c.name as column_name, TYPE_NAME(c.user_type_id) as type, c.max_length" +
            " from sys.columns c inner join sys.objects  o on o.object_id = c.object_id and o. type = 'U'" +
            " where o.name = '%s'" +
            " order by o.name, c.column_id";

    @Override
    public List<SColumnInfo> findColumnsByName(String tableName) {
        String sql = String.format(SQL_FIND_COLUMN_INFO_BY_NAME, tableName);
        return queryForList(SColumnInfo.class, sql);
    }
}
