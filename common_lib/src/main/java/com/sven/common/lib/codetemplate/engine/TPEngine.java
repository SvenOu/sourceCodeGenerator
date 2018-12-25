package com.sven.common.lib.codetemplate.engine;

import com.sven.common.lib.codetemplate.config.TPConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 模板引擎
 *
 * 基于正则表达式生成源码
 * @author sven-ou
 */
public class TPEngine {
    private static Log log = LogFactory.getLog(TPEngine.class);

    public void progress(String tmlPath, String disPath, Map context) throws IOException {
        Path path = Paths.get(tmlPath);

        File parentDir = new File(disPath).getParentFile();
        if(!parentDir.exists()){
            parentDir.mkdirs();
        }

        Charset charset = StandardCharsets.UTF_8;
        String content = new String(Files.readAllBytes(path), charset);

        String result1 = applyStringValues(content, context);
        String result2 = applyArrayValues(result1, context);
//        String result2 = applyArrayValues(content, context);
        Files.write(Paths.get(disPath), result2.getBytes(charset));
    }

    private String applyArrayValues(String content, Map context) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(TPConfig.ARRAY_PATTERN, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String s = matcher.group();
            String replaceText = applyTpRepeat(s, context);
            matcher.appendReplacement(sb, replaceText);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String applyTpRepeat(String repeatStr, Map context) {
        Pattern arrayPat = Pattern.compile(TPConfig.ARRAY_PATTERN_FOR_NAME, Pattern.DOTALL);
        Matcher arrayMatcher = arrayPat.matcher(repeatStr);
        if (!arrayMatcher.find()) {
            return "";
        }
        String arrayNameStr = arrayMatcher.group();
        String arrayFormatName = arrayNameStr.substring(11, arrayNameStr.length() - 3);
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
            if(CaseFormat.SUFFIX_NOT_INCLUDE_END.equals(formats[0]) || CaseFormat.SUFFIX.equals(formats[0])){
                suffix = formats[1];
            }
            if(CaseFormat.PREFIX.equals(formats[0])){
                prefix = formats[1];
            }
            arrayName = arrayFormatName.substring(0, arrayFormatIndex);
        }

        if (context.get(arrayName) == null) {
            return "";
        }
        if (!(context.get(arrayName) instanceof List)) {
            log.error("Error: " + arrayName + "is not a List !");
            return "";
        }
        List<Map> arrayContexts = (List<Map>) context.get(arrayName);
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i<arrayContexts.size(); i++) {
            Map c = arrayContexts.get(i);
            Pattern pat = Pattern.compile(TPConfig.ARRAY_PATTERN_FOR_ATTIBUTE, Pattern.DOTALL);
            Matcher matcher = pat.matcher(repeatStrContent);
            sb.append(prefix);
            while (matcher.find()) {
                String s = matcher.group();
                String key = s.substring(2, s.length() - 1);

                int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
                String formatType = null;
                if (formatIndex > 0) {
                    formatType = key.substring(formatIndex + 1);
                    key = key.substring(0, formatIndex);
                }
                String value = StringUtils.isEmpty(c.get(key)) ? "" : (String) c.get(key);
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

    private String applyStringValues(String content, Map context) {
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(TPConfig.STRING_PATTERN, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(3, s.length() - 2);

            int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
            String formatType = null;
            if (formatIndex > 0) {
                formatType = key.substring(formatIndex + 1);
                key = key.substring(0, formatIndex);
            }
            String value = StringUtils.isEmpty(context.get(key)) ? "" : (String) context.get(key);
            if (formatType != null) {
                value = CaseFormat.formatString(value, formatType);
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
