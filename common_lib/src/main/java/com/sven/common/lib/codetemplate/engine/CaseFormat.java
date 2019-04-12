package com.sven.common.lib.codetemplate.engine;

import com.sven.common.lib.codetemplate.config.TPConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CaseFormat {
    private static Log log = LogFactory.getLog(CaseFormat.class);
    /***************************** formatArray ***********************************/
    public static final String PREFIX = "prefix";
    public static final String SUFFIX = "suffix";
    public static final String SUFFIX_NOT_INCLUDE_END = "suffixNotIncludeEnd";


    /***************************** formatString ***********************************/
    public static final char UNDERLINE = '_';
    public static final String CAMEL_TO_UNDERLINE = "camelToUnderline";
    public static final String UNDERLINE_TO_CAME = "underlineToCame";
    public static final String UNDERLINE_TO_CAMEUPCASE_FIRST = "underlineToCameUPCASEFirst";
    public static final String UP_CASE_FIRST = "upCaseFirst";
    public static final String LOWER_CASE_FIRST = "lowerCaseFirst";
    public static final String UP_CASE_ALL = "upCaseALL";
    public static final String LOWER_CASE_ALL = "lowerCaseALL";

    public static String formatString(String str, String type){
        switch (type){
            case CAMEL_TO_UNDERLINE:
                str = camelToUnderline(str);
                break;
            case UNDERLINE_TO_CAME:
                str = underlineToCame(str);
                break;
            case UNDERLINE_TO_CAMEUPCASE_FIRST:
                str = underlineToCameUPCASEFirst(str);
                break;
            case UP_CASE_FIRST:
                str = upCaseFirst(str);
                break;
            case LOWER_CASE_FIRST:
                str = lowerCaseFirst(str);
                break;
            case UP_CASE_ALL:
                str = str.toUpperCase();
                break;
            case LOWER_CASE_ALL:
                str = str.toLowerCase();
                break;
            default:break;
        }
        return str;
    }

    /**
     *
     * 驼峰 to 下划线
     */
    protected static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线 to 驼峰
     */
    protected static String underlineToCame(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        if(isNumeric(param)){
            return "j_" + param;
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE && (
                    (i + 1 < len) &&
                            !Character.isDigit(param.charAt(i + 1)))
            ) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线 to 驼峰并且第一个字符大写
     */
    protected static String underlineToCameUPCASEFirst(String param) {
        String str = underlineToCame(param);
        return upCaseFirst(str);
    }

    /**
     * 第一个字符大写
     */
    protected static String upCaseFirst(String param) {
        if (!StringUtils.isEmpty(param)) {
            param = param.substring(0, 1).toUpperCase() + param.substring(1);
        }
        return param;
    }
    /**
     * 第一个字符小写
     */
    protected static String lowerCaseFirst(String param) {
        if (!StringUtils.isEmpty(param)) {
            param = param.substring(0, 1).toLowerCase() + param.substring(1);
        }
        return param;
    }

    public static List<Map> getFormatDataMap(Map data, String key, Map rootContext){
        String[] keyArray = key.split("\\s*\\.\\s*");
        try {
            if(TPConfig.STRING_ROOT_SCOPE.equalsIgnoreCase(keyArray[0])){
                return getKeyArrayFormatDataMap(rootContext, Arrays.copyOfRange(keyArray, 1, keyArray.length),0);
            }
            return getKeyArrayFormatDataMap(data, keyArray,0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
    public static List<Map> getKeyArrayFormatDataMap(Map data, String[] keyArray, int index){
        String curKey = keyArray[index];
        Object curData = data.get(curKey);
        index ++;
        if(index >= keyArray.length){
            return (List<Map>) curData;
        }else {
            return getKeyArrayFormatDataMap((Map) curData, keyArray,  index);
        }
    }

    public static String getFormatData(Map data, String key, Map rootData){
        String[] keyArray = key.split("\\s*\\.\\s*");
        try {
            if(TPConfig.STRING_ROOT_SCOPE.equalsIgnoreCase(keyArray[0])){
                return getKeyArrayFormatData(rootData, Arrays.copyOfRange(keyArray, 1, keyArray.length),0);
            }
            return getKeyArrayFormatData(data, keyArray,0);
        } catch (Exception e) {
            log.error(e.getMessage());
            return String.format(TPConfig.FORMAT_ERROR, key);
        }
    }

    public static String getKeyArrayFormatData(Map data, String[] keyArray, int index){
        String curKey = keyArray[index];
        Object curData = data.get(curKey);
        index ++;
        if(index >= keyArray.length){
            return curData.toString();
        }else {
            return getKeyArrayFormatData((Map) curData, keyArray,  index);
        }
    }

    public static boolean isNumeric(String str)
    {
        for (char c : str.toCharArray())
        {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}