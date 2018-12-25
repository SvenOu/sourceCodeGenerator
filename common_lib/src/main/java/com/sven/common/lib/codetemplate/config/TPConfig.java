package com.sven.common.lib.codetemplate.config;

public class TPConfig {
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
}
