package com.sql.code.generator.modules.mysql.dao;


import com.sql.code.generator.modules.mysql.vo.MysqlColumnInfo;
import com.sql.code.generator.modules.mysql.vo.MysqlTableInfo;

import java.util.List;

/**
 * Created by dmitry tkachenko on 1/11/17.
 */
public interface MysqlTableInfoDAO {

    List<MysqlTableInfo> findAllTable();

    List<MysqlColumnInfo> findColumnsByName(String tableName);
}
