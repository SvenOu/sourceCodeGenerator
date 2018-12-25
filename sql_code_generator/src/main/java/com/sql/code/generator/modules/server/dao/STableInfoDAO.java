package com.sql.code.generator.modules.server.dao;


import com.sql.code.generator.modules.server.vo.SColumnInfo;
import com.sql.code.generator.modules.server.vo.STableInfo;

import java.util.List;

/**
 * Created by dmitry tkachenko on 1/11/17.
 */
public interface STableInfoDAO {

    List<STableInfo> findAllTable();

    List<SColumnInfo> findColumnsByName(String tableName);
}
