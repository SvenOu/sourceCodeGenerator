package com.sven.common.lib.codetemplate.engine;

import org.springframework.util.StringUtils;

public class CaseFormat {
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
    public static final String UP_CASE_ALL = "upCaseALL";

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
            case UP_CASE_ALL:
                str = str.toUpperCase();
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
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
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
}