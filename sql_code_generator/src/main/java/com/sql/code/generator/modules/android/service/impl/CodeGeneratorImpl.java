package com.sql.code.generator.modules.android.service.impl;

import com.sql.code.generator.commom.utils.HttpUtils;
import com.sql.code.generator.modules.android.dao.SqliteDao;
import com.sql.code.generator.modules.android.dataBean.SQLiteWithJavaClassType;
import com.sql.code.generator.modules.android.service.CodeGenerator;
import com.sql.code.generator.modules.android.vo.ColumnInfo;
import com.sql.code.generator.modules.android.vo.SqlliteMaster;
import com.sql.code.generator.modules.common.composite.ClassTypeUtils;
import com.sql.code.generator.modules.common.dao.DyDao;
import com.sven.common.lib.codetemplate.engine.CaseFormat;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by sven-ou on 2017/2/16.
 */
@Component("codeGenerator")
public class CodeGeneratorImpl implements CodeGenerator {
    private static Log log = LogFactory.getLog(CodeGeneratorImpl.class);

    @Value("${sql-code.android.dirName}")
    private String codeAndroidDirName;

    @Value("${sql-code-generator.dir}")
    private String generateCodePath;

    @Value("${sql-code-needCopyJavaSourceFile.dir}")
    private String needCopyJavaSourceFilePath;

    @Value("${sql-code-templates.dir}")
    private String codeTemplatesPath;

    @Value("${sql-code.package.vo}")
    private String voPackage;

    @Value("${sql-code.package.dataBean}")
    private String dataBeanPackage;

    @Value("${sql-code.package.tools}")
    private String toolsPackage;

    @Value("${sql-code.package.test}")
    private String testPackage;

    @Value("${sql-code.package.dao}")
    private String daoPackage;

    @Value("${sql-code.package.dao.impl}")
    private String daoImplPackage;

    @Autowired
    private SqliteDao sqliteDao;

    @Autowired
    private TPEngine tpEngine;

    @Override
    public void generateAndroidCode(String driverClassName, String url, String username, String password) throws IOException {
        ((DyDao) sqliteDao).configDyDao(driverClassName, url, username, password);
        String userGenerateCodePath = HttpUtils.appentCurrentSession(generateCodePath);

        Set<String> globleTypesSet = new HashSet<>();
        String voTemplate = "TplVo.java";
        List<String> toolsTemplates = new ArrayList<String>() {{
            add("BeanPropertyCache.java");
            add("BeanPropertyContentValues.java");
            add("BeanPropertyHolder.java");
            add("BeanPropertyRowMapper.java");
            add("DisabledForSql.java");
        }};
        String testTemplate = "TplTest.java";
        String daoTemplate = "TplDao.java";
        String daoImplTemplate = "TplDaoImpl.java";
        String packageName = "com.sven.test";

        FileUtils.deleteDirectory(new File(userGenerateCodePath + codeAndroidDirName));
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
            String voTplPath = codeTemplatesPath + codeAndroidDirName + "/vo/" + voTemplate;
            String voClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST);
            String voDisPath = userGenerateCodePath + codeAndroidDirName + "/vo/" + voClassName + ".java";
            Map context = new HashMap();
            context.put("voPackageName", packageName + "." + voPackage);
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
            tpEngine.progress(voTplPath, voDisPath, context);
            /*------------ end vo ---------------*/


            /* ------------ dao---------------------*/
            // interface
            String daoTplPath = codeTemplatesPath + codeAndroidDirName  + "/dao/" + daoTemplate;
            String daoClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"Dao";
            String daoDisPath = userGenerateCodePath + codeAndroidDirName + "/dao/" + daoClassName + ".java";
            context.put("daoPackageName", packageName + "." + daoPackage);
            context.put("daoClassName", daoClassName);
            tpEngine.progress(daoTplPath, daoDisPath, context);

            //impl
            String daoImplTplPath = codeTemplatesPath + codeAndroidDirName + "/dao/impl/" + daoImplTemplate;
            String daoImplClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"DaoImpl";
            String daoImplDisPath = userGenerateCodePath + codeAndroidDirName + "/dao/impl/" + daoImplClassName + ".java";
            context.put("daoImplPackageName", packageName + "." + daoImplPackage);
            context.put("daoImplClassName", daoImplClassName);
            tpEngine.progress(daoImplTplPath, daoImplDisPath, context);
            /*------------ end dao ---------------*/


            /* ------------ test ---------------------*/
            String testTplPath = codeTemplatesPath + codeAndroidDirName + "/test/" + testTemplate;
            String testClassName = voClassName + "Test";
            String testDisPath = userGenerateCodePath + codeAndroidDirName + "/test/" + testClassName + ".java";
            context.put("testPackageName", packageName + "." + testPackage);
            context.put("testClassName", testClassName);
            tpEngine.progress(testTplPath, testDisPath, context);
            /*------------ end test ---------------*/


            /* ------------ tools ---------------------*/
            FileSystemUtils.copyRecursively(new File(needCopyJavaSourceFilePath + codeAndroidDirName),
                    new File(userGenerateCodePath + codeAndroidDirName + toolsPackage));
            /*------------ end tools ---------------*/
        }

        for (String c : globleTypesSet) {
            //根据此 LOG 调整 SQLiteWithJavaClassType
            log.info(String.format("typesSets.sqlType: %s -> javaType: %s", c, SQLiteWithJavaClassType.getJavaTypeClass(c)));
        }

    }
}
