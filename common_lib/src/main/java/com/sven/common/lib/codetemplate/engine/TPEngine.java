package com.sven.common.lib.codetemplate.engine;

import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.SourceFileInfo;
import com.sven.common.lib.codetemplate.dataBean.TplSourceFileInfo;
import com.sven.common.lib.codetemplate.utils.FileUtils;
import com.sven.common.lib.codetemplate.utils.JsScriptUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板引擎
 * <p>
 * 基于正则表达式生成源码
 *
 * @author sven-ou
 */
public class TPEngine {
    private static Log log = LogFactory.getLog(TPEngine.class);
    private FileGenerator fileGenerator;

    public TPEngine() {
        this.fileGenerator = new FileGenerator();
    }

    public void progressAll(String tplPath, String disPath, Map rootContext) throws IOException {

        TplSourceFileInfo rootInfo = FileUtils.getSourceFileInfo(tplPath);
        FileSystemUtils.deleteRecursively(new File(disPath));

        String tempDirName = "/#tempTemplates/";
        String tempTplsPath = disPath + tempDirName;
        FileGenerator fileGenerator = new FileGenerator();
        Map repeatScopeMapping = fileGenerator.generateTempTpls(rootContext, rootInfo.getPath(), tempTplsPath);
        SourceFileInfo generateTplInfo = FileUtils.getSourceFileInfo(tempTplsPath);

        progressSourceFileInfo(generateTplInfo, tempDirName, rootContext, repeatScopeMapping);
        // FIXME: 可以不删除，用于debug
        FileSystemUtils.deleteRecursively(Paths.get(tempTplsPath));
    }

    private void progressSourceFileInfo(SourceFileInfo tplInfo, String tempDirName, Map rootContext, Map repeatScopeMapping) throws IOException {
        if (tplInfo.isLeaf()) {
            String tplPath = tplInfo.getPath();
            String parentDirPath = new File(tplPath).getParent();
            if(!parentDirPath.substring(parentDirPath.length() -1).equals(TPConfig.PATH_SEPARATOR)){
                parentDirPath += TPConfig.PATH_SEPARATOR;
            }
            String newDirName = parentDirPath
                    .replaceAll("\\\\", TPConfig.PATH_SEPARATOR)
                    .replaceAll(tempDirName, TPConfig.PATH_SEPARATOR);
            String fileName = tplInfo.getName();

            if(tplInfo.isDir()){
                new File(newDirName + '/' + fileName).mkdirs();
                return;
            }
            List<Map> data = null;
            Matcher matcherFileArray = Pattern.compile(TPConfig.FILE_ARRAY_PATTERN, Pattern.DOTALL).matcher(fileName);
            Matcher matcherFileString = Pattern.compile(TPConfig.FILE_STRING_PATTERN, Pattern.DOTALL).matcher(fileName);
            if(matcherFileArray.find()){
                String arrayStr = matcherFileArray.group();
                Matcher matcherFileArrayForName = Pattern.compile(TPConfig.FILE_ARRAY_PATTERN_FOR_NAME, Pattern.DOTALL).matcher(arrayStr);
                if(matcherFileArrayForName.find()){
                    String arrayStrForName = matcherFileArrayForName.group();
                    String arrayStrForNameKey = arrayStrForName.substring(TPConfig.FILE_ARRAY_PATTERN_FOR_NAME_START.length(),
                            arrayStrForName.length() - TPConfig.FILE_ARRAY_PATTERN_FOR_NAME_END.length());
                    int formatIndex = arrayStrForNameKey.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                    if(formatIndex > 0){
                        arrayStrForNameKey = arrayStrForNameKey.substring(0, formatIndex);
                    }

                    if(repeatScopeMapping.containsKey(tplInfo.getParent().getPath())){
                        data= CaseFormat.getFormatDataMap((Map) repeatScopeMapping.get(tplInfo.getParent().getPath()),
                                arrayStrForNameKey, rootContext);
                    }else {
                        data= CaseFormat.getFormatDataMap(rootContext, arrayStrForNameKey, rootContext);
                    }

                    if(data == null || data.size() <= 0){
                        data = new ArrayList<>(0);
                    }

                    String repeatStrContent = arrayStr.substring(arrayStrForName.length(), arrayStr.length() - 2);
                    Matcher matcherFileArrayForAttr = Pattern.compile(TPConfig.FILE_ARRAY_PATTERN_FOR_ATTIBUTE,
                            Pattern.DOTALL).matcher(repeatStrContent);
                    if(matcherFileArrayForAttr.find()){
                        String attrStrForName = matcherFileArrayForAttr.group();
                        String attrKey = attrStrForName.substring(TPConfig.FILE_ARRAY_PATTERN_FOR_ATTIBUTE_START.length(),
                                attrStrForName.length() - TPConfig.FILE_ARRAY_PATTERN_FOR_ATTIBUTE_END.length());

                        int attrFormatIndex = attrKey.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                        String formatType = null;
                        if (attrFormatIndex > 0) {
                            formatType = attrKey.substring(attrFormatIndex + 1);
                            attrKey = attrKey.substring(0, attrFormatIndex);
                        }

                        for (Map d : data) {
                            String name = CaseFormat.getFormatData(d, attrKey, rootContext);
                            if (formatType != null) {
                                name = CaseFormat.formatString(name, formatType);
                            }
                            String newFileName = fileName.replace(arrayStr, repeatStrContent.replace(attrStrForName, name));
                            String newFilePath = newDirName + '/' + newFileName;
                            progress(tplPath, newFilePath, d, rootContext);
                        }
                    }
                }
            }else if(matcherFileString.find()){
                String s = matcherFileString.group();
                String key = s.substring(TPConfig.FILE_STRING_PATTERN_START.length(),
                        s.length() - TPConfig.FILE_STRING_PATTERN_END.length());

                int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                String formatType = null;
                if (formatIndex > 0) {
                    formatType = key.substring(formatIndex + 1);
                    key = key.substring(0, formatIndex);
                }
                String value = CaseFormat.getFormatData(rootContext, key, rootContext);
                if (formatType != null) {
                    value = CaseFormat.formatString(value, formatType);
                }
                fileName = fileName.replace(s, value);
                String newFilePath = newDirName + '/' + fileName;

                String[] keyArray = key.split("\\s*\\.\\s*");
                if(keyArray!= null && keyArray.length >0){
                    progress(tplPath, newFilePath, (Map) rootContext.get(keyArray[0]), rootContext);
                }
            }
            else {//notthing  matcher
                Map scope = null;
                if(repeatScopeMapping.containsKey(tplInfo.getParent().getPath())){
                    scope = (Map) repeatScopeMapping.get(tplInfo.getParent().getPath());
                }else {
                    scope = rootContext;
                }
                String newFilePath = newDirName + '/' + fileName;
                progress(tplPath, newFilePath, scope, rootContext);
            }
        } else {
            List<SourceFileInfo> childs = (List<SourceFileInfo>) tplInfo.getChildren();
            if (null != childs) {
                for (SourceFileInfo c : childs) {
                    progressSourceFileInfo(c, tempDirName, rootContext, repeatScopeMapping);
                }
            }
        }
    }

    public void progress(String tplPath, String disPath, Map context, Map rootContext) throws IOException {
        Path path = Paths.get(tplPath);

        File parentDir = new File(disPath).getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        Charset charset = StandardCharsets.UTF_8;
        String content = new String(Files.readAllBytes(path), charset);

        String result =  applyStringAndArrayValues(content, context, rootContext,
                TPConfig.STRING_PATTERN,
                TPConfig.STRING_PATTERN_START,
                TPConfig.STRING_PATTERN_END,

                TPConfig.ARRAY_PATTERN,
                TPConfig.ARRAY_PATTERN_FOR_NAME,
                TPConfig.ARRAY_PATTERN_FOR_NAME_START,
                TPConfig.ARRAY_PATTERN_FOR_NAME_END,

                TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE,
                TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE_START,
                TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE_END
        );

        Files.write(Paths.get(disPath), result.getBytes(charset));
    }

    private String applyStringAndArrayValues(String content, Map context, Map rootContext,
                                        String stringPattern,
                                        String stringPatternStart,
                                        String stringPatternEnd,
                                        String arrayPattern,
                                        String arrayPatternForName,
                                        String arrayPatternForNameStart,
                                        String arrayPatternForNameEnd,
                                        String arrayPatternForAttribute,
                                        String arrayPatternForAttributeStart,
                                        String arrayPatternForAttributeEnd) {
        String ifPattern = TPConfig.IF_PATTERN;
        String ifPatternForName = TPConfig.IF_PATTERN_FOR_NAME;
        String ifPatternForNameStart = TPConfig.IF_PATTERN_FOR_NAME_START;
        String ifPatternForNameEnd = TPConfig.IF_PATTERN_FOR_NAME_END;
        String ifPatternEnd = TPConfig.IF_PATTERN_END;
        String result1AfterIf = progressIf(content, context, rootContext,
                ifPattern, ifPatternForName, ifPatternForNameStart, ifPatternForNameEnd, ifPatternEnd);
        String result1 = applyStringValues(result1AfterIf, context, rootContext,
                stringPattern, stringPatternStart, stringPatternEnd);

        String result2 = applyMutiArrayValues(result1, context, rootContext, 1);

        String result3 = applyArrayValues(result2, context, rootContext,
                arrayPattern,
                arrayPatternForName,
                arrayPatternForNameStart,
                arrayPatternForNameEnd,
                arrayPatternForAttribute,
                arrayPatternForAttributeStart,
                arrayPatternForAttributeEnd);
        return result3;
    }

    private String applyMutiArrayValues(String content, Map context, Map rootContext, int level) {
        StringBuffer sb = new StringBuffer();

        String arrayPatternLevelText = generateArrayPatternLevelText(level);
        String arrayPattern = String.format(TPConfig.MUTI_ARRAY_PATTERN, arrayPatternLevelText, arrayPatternLevelText);

        level ++;
        Pattern pattern = Pattern.compile(arrayPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String s = matcher.group();
            String replaceText = applyMutiTpRepeat(s, context, rootContext, level, arrayPatternLevelText, arrayPattern);
            matcher.appendReplacement(sb, replaceText);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String applyMutiTpRepeat(String repeatStr, Map context, Map rootContext, int level, String arrayPatternLevelText, String arrayPattern) {
        String arrayPatternForName = String.format(TPConfig.MUTI_ARRAY_PATTERN_FOR_NAME, arrayPatternLevelText);
        Pattern arrayPat = Pattern.compile(arrayPatternForName, Pattern.DOTALL);
        Matcher arrayMatcher = arrayPat.matcher(repeatStr);
        if (!arrayMatcher.find()) {
            return "";
        }
        String arrayNameStr = arrayMatcher.group();
        //$muti-repeat%s(  and  ){{
        String arrayPatternForNameStart = "";
        String arrayPatternForNameStartReg = String.format(TPConfig.MUTI_ARRAY_PATTERN_FOR_NAME_START, arrayPatternLevelText);
        String arrayPatternForNameEnd = String.format(TPConfig.MUTI_ARRAY_PATTERN_FOR_NAME_END, arrayPatternLevelText);
        Matcher arrayPatternForNameStartRegMatcher = Pattern.compile(arrayPatternForNameStartReg, Pattern.DOTALL).matcher(repeatStr);
        if(arrayPatternForNameStartRegMatcher.find()){
            arrayPatternForNameStart = arrayPatternForNameStartRegMatcher.group();
        }else {
            return String.format(TPConfig.FORMAT_ERROR_CONTENT, repeatStr);
        }
        String arrayFormatName = arrayNameStr.substring(arrayPatternForNameStart.length(),
                arrayNameStr.length() - arrayPatternForNameEnd.length());

        String arrayPatternForEnd = "";
        Matcher arrayPatternForEndMatcher = Pattern.compile(String.format(TPConfig.MUTI_ARRAY_PATTERN_FOR_END, arrayPatternLevelText),
                Pattern.DOTALL).matcher(repeatStr);
        if(arrayPatternForEndMatcher.find()){
            arrayPatternForEnd = arrayPatternForEndMatcher.group();
        }else {
            return String.format(TPConfig.FORMAT_ERROR_CONTENT, repeatStr);
        }

        String repeatStrContent = repeatStr.substring(arrayNameStr.length(), repeatStr.length() - arrayPatternForEnd.length());
        String arrayName = arrayFormatName;
        String prefix = "";
        String suffix = "";
        int arrayFormatIndex = arrayFormatName.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
        String arrayFormatType = null;
        String[] formats = new String[2];
        if (arrayFormatIndex > 0) {
            arrayFormatType = arrayFormatName.substring(arrayFormatIndex + 1);
            formats = arrayFormatType.split(TPConfig.ARRAY_FORMAT_SEPARATE_CHAR);
            if (CaseFormat.SUFFIX_NOT_INCLUDE_END.equals(formats[0]) || CaseFormat.SUFFIX.equals(formats[0])) {
                suffix = formats[1];
            }
            if (CaseFormat.PREFIX.equals(formats[0])) {
                prefix = formats[1];
            }
            arrayName = arrayFormatName.substring(0, arrayFormatIndex);
        }
        StringBuffer sb = new StringBuffer();
        List<Map> arrayContexts = CaseFormat.getFormatDataMap(context, arrayName, rootContext);
        if(arrayContexts == null || arrayContexts.size() <=0){
            sb.append(String.format(TPConfig.FORMAT_ERROR, arrayName));
            sb.append(TPConfig.WRAP_CHAR);
            return sb.toString();
        }
        String arrayPatternForAttribute = TPConfig.MUTI_ARRAY_PATTERN_FOR_ATTIBUTE;
        String arrayPatternForAttributeStart = TPConfig.MUTI_ARRAY_PATTERN_FOR_ATTIBUTE_START;
        String arrayPatternForAttributeEnd = TPConfig.MUTI_ARRAY_PATTERN_FOR_ATTIBUTE_END;
        for (int i = 0; i < arrayContexts.size(); i++) {
            Map c = arrayContexts.get(i);
            StringBuffer tempSb1 = new StringBuffer();
            StringBuffer tempSb3 = new StringBuffer();
            Pattern pat = Pattern.compile(arrayPatternForAttribute, Pattern.DOTALL);

            StringBuffer tempSb2 = new StringBuffer();
            List<String> tempContents = new ArrayList<>();
            String tempArrayPatternLevelText = generateArrayPatternLevelText(level);
            String tempArrayPattern = String.format(TPConfig.MUTI_ARRAY_PATTERN, tempArrayPatternLevelText, tempArrayPatternLevelText);
            Pattern tempPattern = Pattern.compile(tempArrayPattern, Pattern.DOTALL);

            Matcher tempMatcher = tempPattern.matcher(repeatStrContent);
            String replaceText = "##TC##";
            while (tempMatcher.find()) {
                String s = tempMatcher.group();
                tempContents.add(s);
                tempMatcher.appendReplacement(tempSb2, replaceText);
            }
            tempMatcher.appendTail(tempSb2);
            tempSb1.append(prefix);

            String tempSb2Str = tempSb2.toString();
            String ifPattern = TPConfig.MUTI_RP_IF_PATTERN;
            String ifPatternForName = TPConfig.MUTI_RP_IF_PATTERN_FOR_NAME;
            String ifPatternForNameStart = TPConfig.MUTI_RP_IF_PATTERN_FOR_NAME_START;
            String ifPatternForNameEnd = TPConfig.MUTI_RP_IF_PATTERN_FOR_NAME_END;
            String ifPatternEnd = TPConfig.MUTI_RP_IF_PATTERN_END;
            tempSb2Str = progressIf(tempSb2Str, c, rootContext,
                    ifPattern, ifPatternForName, ifPatternForNameStart, ifPatternForNameEnd, ifPatternEnd);

            Matcher matcher = pat.matcher(tempSb2Str);
            while (matcher.find()) {
                String s = matcher.group();
                //$(  )
                String key = s.substring(arrayPatternForAttributeStart.length(), s.length() - arrayPatternForAttributeEnd.length());

                int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                String formatType = null;
                if (formatIndex > 0) {
                    formatType = key.substring(formatIndex + 1);
                    key = key.substring(0, formatIndex);
                }
                String value = CaseFormat.getFormatData(c, key, rootContext);
                if (formatType != null) {
                    value = CaseFormat.formatString(value, formatType);
                }
                matcher.appendReplacement(tempSb1, value);
            }
            matcher.appendTail(tempSb1);
            if (!(CaseFormat.SUFFIX_NOT_INCLUDE_END.equals(formats[0]) && i == (arrayContexts.size() - 1))) {
                tempSb1.append(suffix);
            }
            tempSb1.append(TPConfig.WRAP_CHAR);
            String[] tempResultArray = tempSb1.toString().split(replaceText);

            for (int j = 0; j < tempResultArray.length; j++) {
                tempSb3.append(tempResultArray[j]);
                if(j != tempResultArray.length -1){
                    tempSb3.append(tempContents.get(j));
                }
            }

            String tempResult = applyMutiArrayValues(tempSb3.toString(), c, rootContext, level);
            sb.append(tempResult);
        }

        return sb.toString();
    }

    private String generateArrayPatternLevelText(int level) {
        String digitalTextReg = "[0-9]?";
        String digitalSeprator = "-";
        StringBuilder arrayPattern = new StringBuilder();
        for (int i = 0; i < level; i++) {
            if(i >0){
                arrayPattern.append(digitalSeprator);
            }
            arrayPattern.append(digitalTextReg);
        }
        return "(" + arrayPattern.toString() + ")";
    }

    private String applyArrayValues(String content, Map context, Map rootContext,
                                    String arrayPattern,
                                    String arrayPatternForName,
                                    String arrayPatternForNameStart,
                                    String arrayPatternForNameEnd,
                                    String arrayPatternForAttribute,
                                    String arrayPatternForAttributeStart,
                                    String arrayPatternForAttributeEnd) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(arrayPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String s = matcher.group();
            String replaceText = applyTpRepeat(s, context, rootContext,
                    arrayPatternForName,
                    arrayPatternForNameStart,
                    arrayPatternForNameEnd,
                    arrayPatternForAttribute,
                    arrayPatternForAttributeStart,
                    arrayPatternForAttributeEnd);
            matcher.appendReplacement(sb, replaceText);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String applyTpRepeat(String repeatStr, Map context, Map rootContext,
                                 String arrayPatternForName,
                                 String arrayPatternForNameStart,
                                 String arrayPatternForNameEnd,
                                 String arrayPatternForAttribute,
                                 String arrayPatternForAttributeStart,
                                 String arrayPatternForAttributeEnd) {
        Pattern arrayPat = Pattern.compile(arrayPatternForName, Pattern.DOTALL);
        Matcher arrayMatcher = arrayPat.matcher(repeatStr);
        if (!arrayMatcher.find()) {
            return "";
        }
        String arrayNameStr = arrayMatcher.group();
        //$tp-repeat(  and  ){{
        String arrayFormatName = arrayNameStr.substring(arrayPatternForNameStart.length(), arrayNameStr.length() - arrayPatternForNameEnd.length());
        String repeatStrContent = repeatStr.substring(arrayNameStr.length(), repeatStr.length() - 2);

        String arrayName = arrayFormatName;
        String prefix = "";
        String suffix = "";
        int arrayFormatIndex = arrayFormatName.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
        String arrayFormatType = null;
        String[] formats = new String[2];
        if (arrayFormatIndex > 0) {
            arrayFormatType = arrayFormatName.substring(arrayFormatIndex + 1);
            formats = arrayFormatType.split(TPConfig.ARRAY_FORMAT_SEPARATE_CHAR);
            if (CaseFormat.SUFFIX_NOT_INCLUDE_END.equals(formats[0]) || CaseFormat.SUFFIX.equals(formats[0])) {
                suffix = formats[1];
            }
            if (CaseFormat.PREFIX.equals(formats[0])) {
                prefix = formats[1];
            }
            arrayName = arrayFormatName.substring(0, arrayFormatIndex);
        }
        StringBuffer sb = new StringBuffer();
        List<Map> arrayContexts = CaseFormat.getFormatDataMap(context, arrayName, rootContext);
        if(arrayContexts == null || arrayContexts.size() <=0){
            sb.append(String.format(TPConfig.FORMAT_ERROR, arrayName));
            sb.append(TPConfig.WRAP_CHAR);
            return sb.toString();
        }

        for (int i = 0; i < arrayContexts.size(); i++) {
            Map c = arrayContexts.get(i);
            Pattern pat = Pattern.compile(arrayPatternForAttribute, Pattern.DOTALL);

            String ifPattern = TPConfig.RP_IF_PATTERN;
            String ifPatternForName = TPConfig.RP_IF_PATTERN_FOR_NAME;
            String ifPatternForNameStart = TPConfig.RP_IF_PATTERN_FOR_NAME_START;
            String ifPatternForNameEnd = TPConfig.RP_IF_PATTERN_FOR_NAME_END;
            String ifPatternEnd = TPConfig.RP_IF_PATTERN_END;
            repeatStrContent = progressIf(repeatStrContent, c, rootContext,
                    ifPattern, ifPatternForName, ifPatternForNameStart, ifPatternForNameEnd, ifPatternEnd);

            Matcher matcher = pat.matcher(repeatStrContent);
            sb.append(prefix);
            while (matcher.find()) {
                String s = matcher.group();
                //$(  )
                String key = s.substring(arrayPatternForAttributeStart.length(), s.length() - arrayPatternForAttributeEnd.length());

                int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                String formatType = null;
                if (formatIndex > 0) {
                    formatType = key.substring(formatIndex + 1);
                    key = key.substring(0, formatIndex);
                }
                String value = CaseFormat.getFormatData(c, key, rootContext);
                if (formatType != null) {
                    value = CaseFormat.formatString(value, formatType);
                }
                matcher.appendReplacement(sb, value);
            }
            matcher.appendTail(sb);
            if (!(CaseFormat.SUFFIX_NOT_INCLUDE_END.equals(formats[0]) && i == (arrayContexts.size() - 1))) {
                sb.append(suffix);
            }
            sb.append(TPConfig.WRAP_CHAR);
        }
        return sb.toString();
    }

    private String progressIf(String content, Map context, Map rootContext,
                              String ifPattern, String ifPatternForName, String ifPatternForNameStart,
                              String ifPatternForNameEnd, String ifPatternEnd) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(ifPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        boolean hasFind = false;
        String tempSign = "#dollar_sign#";
        while (matcher.find()) {
            hasFind = true;
            //$if(xxx){ xxx $endif}
            String repeatStr = matcher.group();
            Pattern ifPat = Pattern.compile(ifPatternForName, Pattern.DOTALL);
            Matcher ifMatcher = ifPat.matcher(repeatStr);
            if (!ifMatcher.find()) {
                return "";
            }
            //$if(xxx){
            String ifNameStr = ifMatcher.group();
            String ifExcuteble = ifNameStr.substring(ifPatternForNameStart.length(), ifNameStr.length() - ifPatternForNameEnd.length());
            String repeatStrContent = repeatStr.substring(ifNameStr.length(), repeatStr.length() - ifPatternEnd.length());
            Boolean excuteResult = JsScriptUtils.runBooleanScript(ifExcuteble, context, rootContext);
            if(excuteResult){
                repeatStrContent = repeatStrContent.replaceAll("\\$",tempSign);
                matcher.appendReplacement(sb, repeatStrContent);
            }else {
                matcher.appendReplacement(sb, "");
            }
        }
        if(!hasFind){
            return content;
        }else {
            matcher.appendTail(sb);
            return sb.toString()
                    .replaceAll(tempSign,"\\$");
        }
    }
    private String applyStringValues(String content, Map context, Map rootContext,
                                     String stringPattern,
                                     String stringPatternStart,
                                     String stringPatternEnd) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(stringPattern, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String s = matcher.group();
            //${{ }}
            String key = s.substring(stringPatternStart.length(), s.length() - stringPatternEnd.length());

            int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
            String formatType = null;
            if (formatIndex > 0) {
                formatType = key.substring(formatIndex + 1);
                key = key.substring(0, formatIndex);
            }
            String value = CaseFormat.getFormatData(context, key, rootContext);
            if (formatType != null) {
                value = CaseFormat.formatString(value, formatType);
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
    public FileGenerator getFileGenerator() {
        return fileGenerator;
    }

    public void setFileGenerator(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }
}
