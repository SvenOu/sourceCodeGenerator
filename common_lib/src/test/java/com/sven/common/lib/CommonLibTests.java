package com.sven.common.lib;

import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.TplSourceFileInfo;
import com.sven.common.lib.codetemplate.engine.FileGenerator;
import com.sven.common.lib.codetemplate.engine.TPEngine;
import com.sven.common.lib.codetemplate.utils.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonLibApplication.class)
public class CommonLibTests {
    private static Log log = LogFactory.getLog(CommonLibTests.class);

    @Test
    public void contextLoads() throws IOException {
        TPEngine engine = new TPEngine();

        List<Map> fiels = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map c = new HashMap();
            c.put("type", "Type" + i);
            c.put("name", "name" + i);
            fiels.add(c);
        }
        Map context = new HashMap();
        context.put("fiels", fiels);
        context.put("title", "User Page");
        context.put("name", "John Doe");

        ClassPathResource resource = new ClassPathResource("templateDemo/SpLogs.java");
        String tmlPath = resource.getFile().getAbsolutePath();
        String disPath = "E:\\IntenlliJ_IDEA_workspace\\sourceCodeGenerator\\sql_code_generator\\generateCode\\tempFiles\\SpLogs-dis.java";

        engine.progress(tmlPath, disPath, context);
    }

    @Test
    public void testFiles() throws IOException {
        TplSourceFileInfo rootInfo = FileUtils.getSourceFileInfo("E:\\IntenlliJ_IDEA_workspace\\sourceCodeGenerator\\sql_code_generator\\_templates\\test");
        String generateRootPath = "E:/IntenlliJ_IDEA_workspace/sourceCodeGenerator/sql_code_generator/generateCode/tempTpls/";
        FileSystemUtils.deleteRecursively(new File(generateRootPath));
        String rootPath = rootInfo.getPath().replace(rootInfo.getName(), "");

        ArrayList<Map> data = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> context = new HashMap<>();
            context.put("voClassName", "Class" + i);
            data.add(context);
        }
        Map rootData = new HashMap();
        rootData.put("userName", "okMark");
        rootData.put("dirName", "GFile");
        rootData.put("data", data);

        FileGenerator fileGenerator = new FileGenerator();
        fileGenerator.generateTempTpls(rootData, rootPath, generateRootPath, rootInfo);
    }
}
