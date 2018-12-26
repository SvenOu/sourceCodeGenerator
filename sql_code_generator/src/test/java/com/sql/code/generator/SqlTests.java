package com.sql.code.generator;

import com.sql.code.generator.modules.android.dao.SqliteDao;
import com.sql.code.generator.modules.android.dao.impl.SqliteDaoImpl;
import com.sql.code.generator.modules.android.vo.ColumnInfo;
import com.sql.code.generator.modules.android.vo.SqlliteMaster;
import com.sql.code.generator.modules.common.dao.DyDao;
import com.sql.code.generator.modules.server.dao.STableInfoDAO;
import com.sql.code.generator.modules.server.dao.impl.MSSqlDaoImpl;
import com.sql.code.generator.modules.server.vo.SColumnInfo;
import com.sql.code.generator.modules.server.vo.STableInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SqlCodeGeneratorApp.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class SqlTests {
    private static Log log = LogFactory.getLog(SqlTests.class);

    @Autowired
    private STableInfoDAO msSqlDao;

    @Autowired
    private SqliteDao sqliteDao;

    @Test
    public void testServer() {
        String driverClassName = "net.sourceforge.jtds.jdbc.Driver";
        String url = "jdbc:jtds:sqlserver://xxxxxxxxxx";
        String username = "xx";
        String password = "xxxxxx";

        ((DyDao) msSqlDao).configDyDao(driverClassName, url, username, password);
        List<STableInfo> tables = msSqlDao.findAllTable();
        STableInfo mst = tables.get(0);
        List<SColumnInfo> mscs = msSqlDao.findColumnsByName(mst.getName());
        log.debug("");
    }
    @Test
    public void testSqlite() {
        String driverClassName = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/dev_test1.sqlite";
        String username = "";
        String password = "";
        ((DyDao) sqliteDao).configDyDao(driverClassName, url, username, password);
        List<SqlliteMaster> tables1 = sqliteDao.findAllTable();
        SqlliteMaster rs1 = tables1.get(0);
        List<ColumnInfo> r1 = sqliteDao.findColumnsByName(rs1.getName());
        log.debug("");
    }
}
