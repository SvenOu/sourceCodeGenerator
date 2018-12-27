package com.sql.code.generator.modules.common.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sql.code.generator.commom.utils.HttpUtils;
import com.sql.code.generator.modules.android.service.CodeGenerator;
import com.sql.code.generator.modules.common.composite.SqlType;
import com.sql.code.generator.modules.common.dataBean.SourceFileInfo;
import com.sql.code.generator.modules.common.service.CommonService;
import com.sql.code.generator.modules.server.service.SCodeGenerator;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author sven-ou
 */
@Service
public class CommonServiceImpl implements CommonService {
    private static Log log = LogFactory.getLog(CommonServiceImpl.class);

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private SCodeGenerator sCodeGenerator;

    @Value("${sql-code.server.dirName}")
    private String codeServerDirName;

    @Value("${sql-code.android.dirName}")
    private String codeAndroidDirName;

    @Value("${sql-code.tempFilesDir}")
    private String tempFilesDir;

    @Value("${sql-code-generator.dir}")
    private String generateCodePath;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public SourceFileInfo getCodeFileInfo(String type, String url, String username, String password) throws IOException {
        String userGenerateCodePath = HttpUtils.appentCurrentSession(generateCodePath);
        String path = null;
        if (SqlType.SQL_SERVER_2005.getName().equalsIgnoreCase(type)) {
            sCodeGenerator.generateServerCode("net.sourceforge.jtds.jdbc.Driver", url, username, password);
            path = userGenerateCodePath + codeServerDirName;
            return findSourceFileInfo(path);
        } else if (SqlType.SQLLITE.getName().equalsIgnoreCase(type)) {
            codeGenerator.generateAndroidCode("org.sqlite.JDBC", url, username, password);
            path = userGenerateCodePath + codeAndroidDirName;
            return findSourceFileInfo(path);
        }
        return null;
    }

    @Override
    public ResponseEntity<Resource> downloadSourcesFile(String path) throws IOException {
        File file = new File(path);
        if(!file.exists() || file.isDirectory()){
            return ResponseEntity.notFound().build();
        }
        Path fPath = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(fPath));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\""+ file.getName() + "\"");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @Override
    public String generateDirZip(String type) throws ZipException, IOException {
        String userGenerateCodePath = HttpUtils.appentCurrentSession(generateCodePath);
        String path = null;
        if (SqlType.SQL_SERVER_2005.getName().equalsIgnoreCase(type)) {
            path = userGenerateCodePath + codeServerDirName;
        } else if (SqlType.SQLLITE.getName().equalsIgnoreCase(type)) {
            path = userGenerateCodePath + codeAndroidDirName;
        }else {
            return null;
        }

        // Initiate ZipFile object with the path/name of the zip file.
        String dirPath = userGenerateCodePath + tempFilesDir;
        File dir = new File(dirPath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        ZipFile zipFile = new ZipFile(dirPath + "_" + type + "_"
                + RequestContextHolder.currentRequestAttributes().getSessionId() + ".zip");

        // Folder to add
        String folderToAdd = path;

        // Initiate Zip Parameters which define various properties such
        // as compression method, etc.
        ZipParameters parameters = new ZipParameters();

        // set compression method to store compression
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);

        // Set the compression level
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

        // Add folder to the zip file
        zipFile.addFolder(folderToAdd, parameters);

        return zipFile.getFile().getAbsolutePath();
    }


    private void getFileNamesByDirPath(List<String> fileNames,  String path) {
        File folder = new File(path);
        if(!folder.exists()){
            return;
        }
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                getFileNamesByDirPath(fileNames, fileEntry.getAbsolutePath());
            } else {
                fileNames.add(fileEntry.getAbsolutePath());
            }
        }
    }

    @Override
    public String getSourceFileCode(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        StringBuilder builder = new StringBuilder();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            builder.append(currentLine);
            builder.append("\n");
            currentLine = reader.readLine();
        }
        reader.close();
        return builder.toString();
    }

    private SourceFileInfo findSourceFileInfo(String path) {
        File parent = new File(path);
        if (!parent.exists()) {
            return null;
        }

        // for parent
        SourceFileInfo fileInfo = new SourceFileInfo();
        fileInfo.setName(parent.getName());
        fileInfo.setPath(parent.getAbsolutePath());

        // for children
        File[] childs = parent.listFiles();
        if (null == childs || childs.length <= 0) {
            fileInfo.setLeaf(true);
            return fileInfo;
        }
        sortFileChilds(childs);

        List<SourceFileInfo> cfiList = new ArrayList<>(childs.length);
        for (File c : childs) {
            if (!c.exists()) {
                continue;
            }
            SourceFileInfo cfi = findSourceFileInfo(c.getAbsolutePath());
            cfiList.add(cfi);
            fileInfo.setChildren(cfiList);
        }
        return fileInfo;
    }

    /**
     *
     * 根据文件夹类型排列数组
     */
    private void sortFileChilds(File[] childs) {
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                if (f1.isDirectory() && !f2.isDirectory()) {
                    // Directory before non-directory
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    // Non-directory after directory
                    return 1;
                } else {
                    // Alphabetic order otherwise
                    return f1.compareTo(f2);
                }
            }
        };
        Arrays.sort(childs, comp);
    }
}
