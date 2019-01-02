package com.sql.code.generator.modules.mssql.dao;


import com.sql.code.generator.modules.mssql.vo.SColumnInfo;
import com.sql.code.generator.modules.mssql.vo.STableInfo;

import java.util.List;

/**
 * Created by dmitry tkachenko on 1/11/17.
 */
public interface STableInfoDAO {

    List<STableInfo> findAllTable();

    List<SColumnInfo> findColumnsByName(String tableName);
}
