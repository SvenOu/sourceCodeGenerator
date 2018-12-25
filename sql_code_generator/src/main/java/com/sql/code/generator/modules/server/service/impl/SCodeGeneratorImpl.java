package com.sql.code.generator.modules.server.service.impl;

import com.sql.code.generator.commom.utils.HttpUtils;
import com.sql.code.generator.modules.common.composite.ClassTypeUtils;
import com.sql.code.generator.modules.common.dao.DyDao;
import com.sql.code.generator.modules.server.dao.STableInfoDAO;
import com.sql.code.generator.modules.server.dataBean.SClassNameUtil;
import com.sql.code.generator.modules.server.dataBean.SSQLServerWithJavaClassType;
import com.sql.code.generator.modules.server.service.SCodeGenerator;
import com.sql.code.generator.modules.server.vo.SColumnInfo;
import com.sql.code.generator.modules.server.vo.STableInfo;
import com.squareup.javapoet.*;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import com.sven.common.lib.codetemplate.engine.CaseFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Created by sven-ou on 2017/2/16.
 */
@Component("sCodeGenerator")
public class SCodeGeneratorImpl implements SCodeGenerator {
    private static Log log = LogFactory.getLog(SCodeGeneratorImpl.class);

    @Value("${sql-code.server.dirName}")
    private String codeServerDirName;

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

    @Value("${sql-code.package.test}")
    private String testPackage;

    @Value("${sql-code.package.tools}")
    private String toolsPackage;

    @Value("${sql-code.package.dao}")
    private String daoPackage;

    @Value("${sql-code.package.dao.impl}")
    private String daoImplPackage;

    @Autowired
    private TPEngine tpEngine;

    private final STableInfoDAO sTableInfoDAO;

    @Autowired
    public SCodeGeneratorImpl(STableInfoDAO sTableInfoDAO) {
        this.sTableInfoDAO = sTableInfoDAO;
    }

    @Override
    public void generateServerCode(String driverClassName, String url, String username, String password) throws IOException {
        ((DyDao) sTableInfoDAO).configDyDao(driverClassName, url, username, password);

        String userGenerateCodePath = HttpUtils.appentCurrentSession(generateCodePath);

        Set<String> globleTypesSet = new HashSet<>();
        String voTemplate = "TplVo.java";
        List<String> toolsTemplates = new ArrayList<String>() {{
            add("TextUtils.java");
        }};
        String testTemplate = "TplTest.java";
        String daoTemplate = "TplDao.java";
        String daoImplTemplate = "TplDaoImpl.java";
        String packageName = "com.sven.test";

        FileUtils.deleteDirectory(new File(userGenerateCodePath + codeServerDirName));
        List<STableInfo> masters = sTableInfoDAO.findAllTable();
        for (STableInfo table : masters) {
            Set<String> typesSet = new HashSet<>();
            List<SColumnInfo> infos = sTableInfoDAO.findColumnsByName(table.getName());
            ArrayList<Map> sqlFields = new ArrayList<Map>(infos.size());
            ArrayList<Map> fields = new ArrayList<Map>(infos.size());
            ArrayList<Map> fielsImport = new ArrayList<Map>(infos.size());

            for (int i = 0; i < infos.size(); i++) {
                SColumnInfo info = infos.get(i);
                Class cls = SSQLServerWithJavaClassType.getJavaTypeClass(info.getType());
                if (null == cls) {
                    log.error("sql type: " + info.getType() + " cannot find java type !");
                }
                String fieldName = CaseFormat.formatString(info.getColumnName(), CaseFormat.UNDERLINE_TO_CAME);

                typesSet.add(cls.getName());
                globleTypesSet.add(info.getType());

                Map sqlfielContext = new HashMap();
                sqlfielContext.put("name", info.getColumnName());
                sqlFields.add(sqlfielContext);

                Map fieldContext = new HashMap();
                fieldContext.put("type", cls.getSimpleName());
                fieldContext.put("name", info.getColumnName());
                fields.add(fieldContext);

            }
            /* ------------ vo ---------------------*/
            String voTplPath = codeTemplatesPath + codeServerDirName + "/vo/" + voTemplate;
            String voClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST);;
            String voDisPath = userGenerateCodePath + codeServerDirName + "/vo/" + voClassName + ".java";
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
            String daoTplPath = codeTemplatesPath + codeServerDirName  + "/dao/" + daoTemplate;
            String daoClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"Dao";
            String daoDisPath = userGenerateCodePath + codeServerDirName + "/dao/" + daoClassName + ".java";
            context.put("daoPackageName", packageName + "." + daoPackage);
            context.put("daoClassName", daoClassName);
            tpEngine.progress(daoTplPath, daoDisPath, context);

            //impl
            String daoImplTplPath = codeTemplatesPath + codeServerDirName + "/dao/impl/" + daoImplTemplate;
            String daoImplClassName = CaseFormat.formatString(table.getName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"DaoImpl";
            String daoImplDisPath = userGenerateCodePath + codeServerDirName + "/dao/impl/" + daoImplClassName + ".java";
            context.put("daoImplPackageName", packageName + "." + daoImplPackage);
            context.put("daoImplClassName", daoImplClassName);
            tpEngine.progress(daoImplTplPath, daoImplDisPath, context);
            /*------------ end dao ---------------*/


            /* ------------ test ---------------------*/
            String testTplPath = codeTemplatesPath + codeServerDirName + "/test/" + testTemplate;
            String testClassName = voClassName + "Test";
            String testDisPath = userGenerateCodePath + codeServerDirName + "/test/" + testClassName + ".java";
            context.put("testPackageName", packageName + "." + testPackage);
            context.put("testClassName", testClassName);
            tpEngine.progress(testTplPath, testDisPath, context);
            /*------------ end test ---------------*/

            /* ------------ tools ---------------------*/
            FileSystemUtils.copyRecursively(new File(needCopyJavaSourceFilePath + codeServerDirName),
                    new File(userGenerateCodePath + codeServerDirName + toolsPackage));
            /*------------ end tools ---------------*/
        }

        for (String c : globleTypesSet) {
            //根据此 LOG 调整 SQLiteWithJavaClassType
            log.info(String.format("typesSets.sqlType: %s -> javaType: %s", c, SSQLServerWithJavaClassType.getJavaTypeClass(c)));
        }
    }
}
