package com.sven.common.lib.codetemplate.config;

public class TPConfig {
    /**
     * 用于文件夹的正则表达式
     */
    public static final String DIR_STRING_PATTERN = "\\$dir\\{\\{[\\S\\s]+?(}+)";
    /**
     * 查找单个string 数值的正则表达式
     */
    public static final String STRING_PATTERN = "\\$\\{\\{[\\S\\s]+?(}+)";

    /**
     * 查找数组数值的正则表达式
     */
    public static final String ARRAY_PATTERN = "(\\$tp-repeat\\(.+?\\{\\{).+?}}";
    public static final String ARRAY_PATTERN_FOR_NAME = "\\$tp-repeat\\(.+?\\)\\{\\{";
    public static final String ARRAY_PATTERN_FOR_ATTIBUTE = "\\$\\(.+?\\)";

    public static final String FORMAT_SEPARATE_CHAR = "-";
    public static final String ARRAY_FORMAT_SEPARATE_CHAR = "~";
    public static final String WRAP_CHAR = "";
    public static final String PATH_SEPARATOR = "/";

    // 文件名
    public static final String KEY_DATA = "data";
    public static final String KEY_USER_FILES = "UserFiles";

    //错误反馈表达式
    public static final String FORMAT_ERROR = "[errorForKey-%s]";

}
