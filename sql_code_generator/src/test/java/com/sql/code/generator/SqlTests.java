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
    public void testDaos() {

        String driverClassName = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/dev_test1.sqlite";
        String username = "";
        String password = "";
        ((DyDao) sqliteDao).configDyDao(driverClassName, url, username, password);
        List<SqlliteMaster> tables1 = sqliteDao.findAllTable();
        SqlliteMaster rs1 = tables1.get(0);
        List<ColumnInfo> r1 = sqliteDao.findColumnsByName(rs1.getName());
        log.debug("");

        driverClassName = "net.sourceforge.jtds.jdbc.Driver";
        url = "jdbc:jtds:sqlserver://xxxx";
        username = "xx";
        password = "xxxxxx";

        ((DyDao) msSqlDao).configDyDao(driverClassName, url, username, password);
        List<STableInfo> tables = msSqlDao.findAllTable();
        STableInfo mst = tables.get(0);
        List<SColumnInfo> mscs = msSqlDao.findColumnsByName(mst.getName());
        log.debug("");
    }

    @Test
    public void test1() {
        List<SqlliteMaster> results = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/dev_test1.sqlite");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sqlite_master WHERE type = 'table'");
            BeanPropertyRowMapper<SqlliteMaster> rowerMappper = BeanPropertyRowMapper.newInstance(SqlliteMaster.class);
            int i = 0;
            while (rs.next()) {
                SqlliteMaster sqlliteMaster = rowerMappper.mapRow(rs, i);
                results.add(sqlliteMaster);
                i++;
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
    @Test
    public void test2() {
        List<STableInfo> results = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            c = DriverManager.getConnection("jdbc:jtds:sqlserver://xxx", "xx", "xxxxxx");
            c.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM sys.objects where type = 'U'");
            BeanPropertyRowMapper<STableInfo> rowerMappper = BeanPropertyRowMapper.newInstance(STableInfo.class);
            int i = 0;
            while (rs.next()) {
                STableInfo sqlliteMaster = rowerMappper.mapRow(rs, i);
                results.add(sqlliteMaster);
                i++;
                System.out.println();
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Operation done successfully");
    }
}
