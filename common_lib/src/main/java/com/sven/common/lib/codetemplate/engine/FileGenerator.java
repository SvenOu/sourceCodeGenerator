package com.sven.common.lib.codetemplate.engine;

import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.TplSourceFileInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileGenerator {
    private static Log log = LogFactory.getLog(FileGenerator.class);

    public void generateTempTpls(Map rootData, String rootPath, String tempRootPath) throws IOException {
        FileSystemUtils.copyRecursively(Paths.get(rootPath), Paths.get(tempRootPath));
        modifyDirs(tempRootPath, rootData);
    }

    private void modifyDirs(String rootPath, Map rootData) throws IOException {
        File tempRootDir = new File(rootPath);
        File[] childFiles = tempRootDir.listFiles();
        if(childFiles == null || childFiles.length <= 0){
            return;
        }
        for(File f: childFiles){
            if(f.isDirectory()){
                Matcher arrayRootMatcher = Pattern.compile(TPConfig.DIR_ARRAY_PATTERN, Pattern.DOTALL)
                        .matcher(f.getName());

                Matcher stringMatcher = Pattern.compile(TPConfig.DIR_STRING_PATTERN, Pattern.DOTALL)
                        .matcher(f.getName());

                if (arrayRootMatcher.find()) {
                    String repeatStr = arrayRootMatcher.group();
                    Pattern arrayPat = Pattern.compile(TPConfig.DIR_ARRAY_PATTERN_FOR_NAME, Pattern.DOTALL);
                    Matcher arrayMatcher = arrayPat.matcher(repeatStr);
                    if (!arrayMatcher.find()) {
                        continue;
                    }
                    String arrayNameStr = arrayMatcher.group();
                    //$dir-repeat(  and  ){{
                    String arrayName = arrayNameStr.substring(TPConfig.DIR_ARRAY_PATTERN_FOR_NAME_START.length(),
                            arrayNameStr.length() - TPConfig.DIR_ARRAY_PATTERN_FOR_NAME_END.length());
                    String repeatStrContent = repeatStr.substring(arrayNameStr.length(), repeatStr.length() - 2);

                    List<Map> arrayContexts = CaseFormat.getFormatDataMap(rootData, arrayName);
                    if(arrayContexts == null || arrayContexts.size() <=0){
                        String name = String.format(TPConfig.FORMAT_ERROR, arrayName);
                        f.renameTo(new File(f.getParent() + "/" + name));
                        continue;
                    }
                    for (int i = 0; i < arrayContexts.size(); i++) {
                        StringBuffer newFName = new StringBuffer();
                        Map c = arrayContexts.get(i);
                        Pattern pat = Pattern.compile(TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE, Pattern.DOTALL);
                        Matcher matcher = pat.matcher(repeatStrContent);
                        while (matcher.find()) {
                            String s = matcher.group();
                            //$(  )
                            String key = s.substring(TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE_START.length(),
                                    s.length() - TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE_END.length());

                            int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                            String formatType = null;
                            if (formatIndex > 0) {
                                formatType = key.substring(formatIndex + 1);
                                key = key.substring(0, formatIndex);
                            }
                            String value = CaseFormat.getFormatData(c, key);
                            if (formatType != null) {
                                value = CaseFormat.formatString(value, formatType);
                            }
                            matcher.appendReplacement(newFName, value);
                        }
                        matcher.appendTail(newFName);
                        File newDirFile = new File(f.getParent() + "/" + newFName.toString() + "/");
                        if(!newDirFile.exists()){
                            newDirFile.mkdir();
                        }
                        FileSystemUtils.copyRecursively(f, newDirFile);
                        modifyDirs(newDirFile.getAbsolutePath(), c);
                    }
                    FileSystemUtils.deleteRecursively(f);

                }else if (stringMatcher.find()){
                    StringBuffer sb = new StringBuffer();
                    String s = stringMatcher.group();
                    String key = s.substring(TPConfig.DIR_STRING_PATTERN_START.length(),
                            s.length() - TPConfig.DIR_STRING_PATTERN_END.length());
                    int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                    String formatType = null;
                    if (formatIndex > 0) {
                        formatType = key.substring(formatIndex + 1);
                        key = key.substring(0, formatIndex);
                    }
                    String value = CaseFormat.getFormatData(rootData, key);
                    if (formatType != null) {
                        value = CaseFormat.formatString(value, formatType);
                    }
                    stringMatcher.appendReplacement(sb, value);
                    stringMatcher.appendTail(sb);
                    String dirName = f.getName().replace(s, sb);
                    File newDirFile = new File(f.getParent() + "/"+ dirName);
                    f.renameTo(newDirFile);
                    modifyDirs(newDirFile.getAbsolutePath(), rootData);
                }
            }
        }
    }
}
