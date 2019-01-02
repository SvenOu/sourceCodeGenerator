package com.sql.code.generator;

import com.sql.code.generator.modules.common.service.CodeGenerator;
import com.sql.code.generator.modules.sqlite.dao.SqliteDao;
import com.sql.code.generator.modules.common.composite.SqlType;
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
    private CodeGenerator codeGenerator;

    @Autowired
    @Qualifier("mssql")
    private CodeGenerator sCodeGenerator;

    @Autowired
    private CommonService commonService;

    @Value("${sql-code-templates.dir}")
    private String templatesDirPath;

    @Value("${sql-code-templates.default.name}")
    private String defaultTemplatesDirName;

    @Value("${sql-code-generator.dir}")
    private String generatorDirPath;

    @Test
    public void generateCodeFiles() throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get("E:\\IntenlliJ_IDEA_workspace\\sourceCodeGenerator\\sql_code_generator\\generateCode"));

        String packageName = "com.test.sven1";

        String driveClass = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/dev_test2.sqlite";
        String username = "";
        String password = "";
        codeGenerator.generateCodeFiles(packageName, driveClass, url, username, password);


        String driveClass2 = "net.sourceforge.jtds.jdbc.Driver";
        String url2 = "jdbc:jtds:sqlserver://sql30.easternphoenix.com:1433/ChurchsYMTC";
        String username2 = "xx";
        String password2 = "xxxxxx";
        sCodeGenerator.generateCodeFiles(packageName, driveClass2, url2, username2, password2);

    }

    @Test
    public void downloadCodeFile() throws IOException {
        String packageName = "com.test.sven1";

        String url = "jdbc:jtds:sqlserver://sql30.easternphoenix.com:1433/ChurchsYMTC";
        String username = "xx";
        String password = "xxxxxx";
        SourceFileInfo info1 = commonService.getCodeFileInfo(packageName, SqlType.SQL_SERVER_2005.getName(), url, username, password);
        log.debug("");


        String url2 = "jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/SourceGenerator.sqlite3";
        String username2 = "";
        String password2 = "";
        SourceFileInfo info2 = commonService.getCodeFileInfo(packageName, SqlType.SQLLITE.getName(), url2, username2, password2);
        log.debug("");
    }

    @Test
    public void generateDirZip() throws IOException, ZipException {
        commonService.generateDirZip(SqlType.SQL_SERVER_2005.getName());
        commonService.generateDirZip(SqlType.SQLLITE.getName());
        log.debug("");
    }
}
