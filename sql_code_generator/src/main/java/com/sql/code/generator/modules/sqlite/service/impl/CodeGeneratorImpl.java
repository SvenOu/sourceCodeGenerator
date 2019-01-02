package com.sql.code.generator.modules.sqlite.service.impl;

import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.service.CodeGenerator;
import com.sql.code.generator.modules.sqlite.dao.SqliteDao;
import com.sql.code.generator.modules.sqlite.dataBean.SQLiteWithJavaClassType;
import com.sql.code.generator.modules.sqlite.vo.ColumnInfo;
import com.sql.code.generator.modules.sqlite.vo.SqlliteMaster;
import com.sql.code.generator.modules.common.composite.ClassTypeUtils;
import com.sql.code.generator.modules.common.dao.DyDao;
import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.engine.CaseFormat;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by sven-ou on 2017/2/16.
 */
@Component("sqlite")
public class CodeGeneratorImpl implements CodeGenerator {
    private static Log log = LogFactory.getLog(CodeGeneratorImpl.class);

    @Value("${sql-code-templates.dir}")
    private String templatesDirPath;

    @Value("${sql-code-templates.default.name}")
    private String defaultTemplatesDirName;

    @Value("${sql-code-generator.dir}")
    private String generatorDirPath;

    @Value("${sql-code-templates.sqlite.name}")
    private String sqlite;

    @Autowired
    private SqliteDao sqliteDao;

    @Autowired
    private TPEngine tpEngine;

    @Override
    public Map generateCodeModel(String packageName, String driverClassName, String url, String username, String password) {
        Map rootContext = new HashMap();
        List<Map> data = new ArrayList<>();
        ((DyDao) sqliteDao).configDyDao(driverClassName, url, username, password);
        Set<String> globleTypesSet = new HashSet<>();
        List<SqlliteMaster> masters = sqliteDao.findAllTable();
        for (SqlliteMaster table : masters) {
            Set<String> typesSet = new HashSet<>();
            List<ColumnInfo> infos = sqliteDao.findColumnsByName(table.getName());
            ArrayList<Map> sqlFields = new ArrayList<Map>(infos.size());
            ArrayList<Map> fields = new ArrayList<Map>(infos.size());
            ArrayList<Map> fielsImport = new ArrayList<Map>(infos.size());

            for (int i = 0; i < infos.size(); i++) {
                ColumnInfo info = infos.get(i);
                Class cls = SQLiteWithJavaClassType.getJavaTypeClass(info.getType());
                if (null == cls) {
                    log.error("sql type: " + info.getType() + " cannot find java type !");
                }
                String fieldName = CaseFormat.formatString(info.getName(), CaseFormat.UNDERLINE_TO_CAME);

                typesSet.add(cls.getName());
                globleTypesSet.add(info.getType());

                Map sqlfielContext = new HashMap();
                sqlfielContext.put("name", info.getName());
                sqlFields.add(sqlfielContext);

                Map fieldContext = new HashMap();
                fieldContext.put("type", cls.getSimpleName());
                fieldContext.put("name", info.getName());
                fields.add(fieldContext);

            }
            /* ------------ vo ---------------------*/
            String voClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST);
            Map context = new HashMap();
            context.put("voPackageName", packageName + ".vo");
            context.put("voSqlName", table.getName());
            context.put("voClassName", voClassName);
            context.put("sqlFields", sqlFields);
            context.put("fields", fields);

            for (String type : typesSet) {
                if (!ClassTypeUtils.isPrimitiveWrapper(type)) {
                    Map fielImportContext = new HashMap();
                    fielImportContext.put("name", type);
                    fielsImport.add(fielImportContext);
                }
            }
            context.put("fielsImport", fielsImport);
            /*------------ end vo ---------------*/


            /* ------------ dao---------------------*/
            // interface
            String daoClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"Dao";
            context.put("daoPackageName", packageName + ".dao");
            context.put("daoClassName", daoClassName);

            //impl
            String daoImplClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"DaoImpl";

            context.put("daoImplPackageName", packageName + ".impl");
            context.put("daoImplClassName", daoImplClassName);
            /*------------ end dao ---------------*/


            /* ------------ test ---------------------*/
            String testClassName = voClassName + "Test";
            context.put("testPackageName", packageName + ".test");
            context.put("testClassName", testClassName);
            /*------------ end test ---------------*/

            data.add(context);
        }
        rootContext.put("data", data);
        for (String c : globleTypesSet) {
            //根据此 LOG 调整 SQLiteWithJavaClassType
            log.info(String.format("typesSets.sqlType: %s -> javaType: %s", c, SQLiteWithJavaClassType.getJavaTypeClass(c)));
        }
        return rootContext;
    }

    @Override
    public String generateCodeFiles(String packageName, String driverClassName, String url, String username, String password) throws IOException {
        Map rootContext = generateCodeModel(packageName, driverClassName, url, username, password);
        String sessionId = "empty_sessionId";
        String userName = "empty_userName";

        if(SecurityUtils.getCurrentSessionId() != null){
            sessionId = SecurityUtils.getCurrentSessionId();
        }
        if(SecurityUtils.getCurrentUserDetails() != null){
            userName = SecurityUtils.getCurrentUserDetails().getUsername();
        }
        rootContext.put("userName", userName);
        rootContext.put("sessionId", sessionId);

        String tplPath = templatesDirPath + defaultTemplatesDirName + sqlite;
        String disPath = generatorDirPath + sqlite;
        FileSystemUtils.deleteRecursively(Paths.get(disPath));
        tpEngine.progressAll(tplPath,
                disPath,
                rootContext);
        return generatorDirPath + TPConfig.KEY_USER_FILES + '/' + sqlite;
    }
}
