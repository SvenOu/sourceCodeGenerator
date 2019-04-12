package com.sql.code.generator;

import com.sql.code.generator.modules.common.service.CodeGenerator;
import com.sql.code.generator.modules.sqlite.dao.SqliteDao;
import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sql.code.generator.modules.common.service.CommonService;
import com.sql.code.generator.modules.mssql.dao.STableInfoDAO;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SqlCodeGeneratorApp.class)
public class SqlCodeGeneratorApplicationTests {
    private static Log log = LogFactory.getLog(SqlCodeGeneratorApplicationTests.class);

    @Autowired
    private SqliteDao sqliteDao;

    @Autowired
    private STableInfoDAO sTableInfoDAO;


    @Autowired
    @Qualifier("sqlite")
    private CodeGenerator sqliteCodeGenerator;

    @Autowired
    @Qualifier("mssql")
    private CodeGenerator mssqlCodeGenerator;

    @Autowired
    private CommonService commonService;

    @Value("${sql-code-templates.dir}")
    private String templatesDirPath;

    @Value("${sql-code-templates.default.name}")
    private String defaultTemplatesDirName;

    @Value("${sql-code-generator.dir}")
    private String generatorDirPath;

    @Value("${sql-code-templates.sqlite.name}")
    private String sqlite;

    @Value("${sql-code-templates.mssql.type.name}")
    private String mssql;

    @Test
    public void generateCodeFiles() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get("E:\\IntenlliJ_IDEA_workspace\\sourceCodeGenerator\\sql_code_generator\\generateCode"));

        String packageName = "com.test.sven1";

        String driveClass = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/SourceGenerator.sqlite3";
        String username = "";
        String password = "";

        String tplPath = templatesDirPath + defaultTemplatesDirName  + '/' + sqlite + "_server/";
        String disPath = generatorDirPath + sqlite + "_server/";
        Map rootContext = sqliteCodeGenerator.generateCodeModel(packageName, driveClass, url, username, password);
        mssqlCodeGenerator.generateCodeFiles(rootContext, tplPath, disPath);


//        String driveClass2 = "net.sourceforge.jtds.jdbc.Driver";
//        String url2 = "jdbc:jtds:sqlserver://sql30.easternphoenix.com:1433/ChurchsYMTC";
//        String username2 = "xx";
//        String password2 = "xxxxxx";
//
//        String tplPath2 = templatesDirPath + defaultTemplatesDirName  + '/' + mssql + "_server/";
//        String disPath2 = generatorDirPath  + mssql + "_server/";
//
//        Map rootContext2 = mssqlCodeGenerator.generateCodeModel(packageName, driveClass2, url2, username2, password2);
//        mssqlCodeGenerator.generateCodeFiles(rootContext2, tplPath2, disPath2);

    }

    @Test
    public void generateDirZip() throws IOException, ZipException {
//        commonService.generateDirZip(SqlType.SQL_SERVER_2005.getName());
//        commonService.generateDirZip(SqlType.SQLLITE.getName());
//        log.debug("");
    }
}
