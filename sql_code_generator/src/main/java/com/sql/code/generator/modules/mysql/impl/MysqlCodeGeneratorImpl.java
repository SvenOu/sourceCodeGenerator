package com.sql.code.generator.modules.mysql.impl;

import com.sql.code.generator.commom.utils.SecurityUtils;
import com.sql.code.generator.modules.common.composite.ClassTypeUtils;
import com.sql.code.generator.modules.common.dao.DyDao;
import com.sql.code.generator.modules.common.service.CodeGenerator;
import com.sql.code.generator.modules.mysql.dao.MysqlTableInfoDAO;
import com.sql.code.generator.modules.mysql.dataBean.MysqlServerWithJavaClassType;
import com.sql.code.generator.modules.mysql.vo.MysqlColumnInfo;
import com.sql.code.generator.modules.mysql.vo.MysqlTableInfo;
import com.sven.common.lib.codetemplate.engine.CaseFormat;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Component("mysql")
public class MysqlCodeGeneratorImpl implements CodeGenerator {
    private static Log log = LogFactory.getLog(MysqlCodeGeneratorImpl.class);

    @Autowired
    private TPEngine tpEngine;

    private final MysqlTableInfoDAO mysqlTableInfoDAO;

    @Autowired
    public MysqlCodeGeneratorImpl(MysqlTableInfoDAO sTableInfoDAO) {
        this.mysqlTableInfoDAO = sTableInfoDAO;
    }

    @Override
    public Map generateCodeModel(String packageName, String driverClassName, String url, String username, String password) {
        Map rootContext = new HashMap();
        List<Map> data = new ArrayList<>();
        String databaseName;
        if(url.contains("?")){
            databaseName = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf("?"));
        }else {
            databaseName = url.substring(url.lastIndexOf("/") + 1);
        }
        ((DyDao) mysqlTableInfoDAO).configDyDao(driverClassName, url, username, password, databaseName);
        Set<String> globleTypesSet = new HashSet<>();
        List<MysqlTableInfo> masters = mysqlTableInfoDAO.findAllTable();
        for (MysqlTableInfo table : masters) {
            Set<String> typesSet = new HashSet<>();
            List<MysqlColumnInfo> infos = mysqlTableInfoDAO.findColumnsByName(table.getTableName());
            ArrayList<Map> sqlFields = new ArrayList<Map>(infos.size());
            ArrayList<Map> fields = new ArrayList<Map>(infos.size());
            ArrayList<Map> fielsImport = new ArrayList<Map>(infos.size());

            for (int i = 0; i < infos.size(); i++) {
                MysqlColumnInfo info = infos.get(i);
                Class cls = MysqlServerWithJavaClassType.getJavaTypeClass(info.getType());
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
            String voClassName = CaseFormat.formatString(table.getTableName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST);;
            Map context = new HashMap();

            context.put("voPackageName", packageName + ".vo");
            context.put("voSqlName", table.getTableName());
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
            String daoClassName = CaseFormat.formatString(table.getTableName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"Dao";
            context.put("daoPackageName", packageName + ".dao");
            context.put("daoClassName", daoClassName);

            //impl
            String daoImplClassName = CaseFormat.formatString(table.getTableName(), CaseFormat.UNDERLINE_TO_CAMEUPCASE_FIRST)+"DaoImpl";
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
            log.info(String.format("typesSets.sqlType: %s -> javaType: %s", c, MysqlServerWithJavaClassType.getJavaTypeClass(c)));
        }
        return rootContext;
    }

    @Override
    public void generateCodeFiles(Map rootContext, String tplDirPath, String disPath) throws IOException {
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

        FileSystemUtils.deleteRecursively(Paths.get(disPath));
        tpEngine.progressAll(tplDirPath,
                disPath,
                rootContext);
    }
}
