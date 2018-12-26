package com.sql.code.generator;

import com.sql.code.generator.modules.android.dao.SqliteDao;
import com.sql.code.generator.modules.android.service.CodeGenerator;
import com.sql.code.generator.modules.common.composite.SqlType;
import com.sql.code.generator.modules.common.dataBean.SourceFileInfo;
import com.sql.code.generator.modules.common.service.CommonService;
import com.sql.code.generator.modules.server.dao.STableInfoDAO;
import com.sql.code.generator.modules.server.service.SCodeGenerator;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SqlCodeGeneratorApp.class)
public class SqlCodeGeneratorApplicationTests {
    private static Log log = LogFactory.getLog(SqlCodeGeneratorApplicationTests.class);

    @Autowired
    private SqliteDao sqliteDao;

    @Autowired
    private STableInfoDAO sTableInfoDAO;
    @Autowired
    private CodeGenerator codeGenerator;
    @Autowired
    private SCodeGenerator sCodeGenerator;

    @Autowired
    private CommonService commonService;

    @Value("${sql-code-generator.dir}")
    private String generateCodePath;

    @Value("${sql-code.server.dirName}")
    private String codeServerDirName;

    @Value("${sql-code.android.dirName}")
    private String codeAndroidDirName;

    @Test
    public void downloadCodeFile() throws IOException {
        String url = "jdbc:jtds:sqlserver://xxxxxxxxxx";
        String username = "xx";
        String password = "xxxxxx";
        SourceFileInfo info1 = commonService.getCodeFileInfo(SqlType.SQL_SERVER_2005.getName(), url, username, password);
        log.debug("");

        String url2 = "jdbc:sqlite:E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/db/SourceGenerator.sqlite3";
        String username2 = "";
        String password2 = "";
        SourceFileInfo info2 = commonService.getCodeFileInfo(SqlType.SQLLITE.getName(), url2, username2, password2);
        log.debug("");
    }

    @Test
    public void generateDirZip() throws IOException, ZipException {
        commonService.generateDirZip(SqlType.SQL_SERVER_2005.getName());
        commonService.generateDirZip(SqlType.SQLLITE.getName());
        log.debug("");
    }
}
