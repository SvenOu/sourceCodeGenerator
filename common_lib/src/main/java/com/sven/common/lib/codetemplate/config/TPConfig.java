package com.sven.common.lib.codetemplate.config;

public class TPConfig {
    /**
     * 引用数据的根
     */
    public static final String STRING_ROOT_SCOPE = "#root";

    /**
     * 查找单个string 数值的正则表达式
     */
    public static final String STRING_PATTERN = "\\$\\{\\{[\\S\\s]+?(}+)";
    public static final String STRING_PATTERN_START = "${{";
    public static final String STRING_PATTERN_END = "}}";

    /**
     * 查找数组数值的正则表达式
     */
    public static final String ARRAY_PATTERN = "(\\$tp-repeat\\(.+?\\{\\{).+?}}";

    public static final String ARRAY_PATTERN_FOR_NAME = "\\$tp-repeat\\(.+?\\)\\{\\{";
    public static final String ARRAY_PATTERN_FOR_NAME_START = "$tp-repeat(";
    public static final String ARRAY_PATTERN_FOR_NAME_END = "){{";

    public static final String ARRAY_PATTERN_FOR_ATTIBUTE = "\\$\\(.+?\\)";
    public static final String ARRAY_PATTERN_FOR_ATTIBUTE_START = "$(";
    public static final String ARRAY_PATTERN_FOR_ATTIBUTE_END = ")";

    /**
     * 多重数组正则表达式
     */
    public static final String MUTI_ARRAY_PATTERN = "(\\$muti-repeat%s\\(.+?\\{\\{).+?\\$end%s}}";
//    public static final String MUTI_ARRAY_PATTERN_LEVEL = "([0-9]?-[0-9]?)";

    public static final String MUTI_ARRAY_PATTERN_FOR_NAME = "\\$muti-repeat%s\\(.+?\\)\\{\\{";
    public static final String MUTI_ARRAY_PATTERN_FOR_NAME_START = "\\$muti-repeat%s\\(";
    public static final String MUTI_ARRAY_PATTERN_FOR_NAME_END = "){{";
    public static final String MUTI_ARRAY_PATTERN_FOR_END = "\\$end%s}}";

    public static final String MUTI_ARRAY_PATTERN_FOR_ATTIBUTE = "\\$\\(.+?\\)";
    public static final String MUTI_ARRAY_PATTERN_FOR_ATTIBUTE_START = "$(";
    public static final String MUTI_ARRAY_PATTERN_FOR_ATTIBUTE_END = ")";

    public static final String FORMAT_SEPARATE_CHAR = "-";
    public static final String ARRAY_FORMAT_SEPARATE_CHAR = "~";
    public static final String WRAP_CHAR = "";
    public static final String PATH_SEPARATOR = "/";

    /**
     * 各种 if 的正则表达式
     */

    public static final String IF_PATTERN = "\\$if\\(.+?\\)\\{.+?\\$endif\\}";
    public static final String IF_PATTERN_FOR_NAME = "\\$if\\(.+?\\)\\{";
    public static final String IF_PATTERN_FOR_NAME_START = "$if(";
    public static final String IF_PATTERN_FOR_NAME_END = "){";
    public static final String IF_PATTERN_END = "$endif}";

    public static final String RP_IF_PATTERN = "\\$rpif\\(.+?\\)\\{.+?\\$endrpif\\}";
    public static final String RP_IF_PATTERN_FOR_NAME = "\\$rpif\\(.+?\\)\\{";
    public static final String RP_IF_PATTERN_FOR_NAME_START = "$rpif(";
    public static final String RP_IF_PATTERN_FOR_NAME_END = "){";
    public static final String RP_IF_PATTERN_END = "$endrpif}";

    public static final String MUTI_RP_IF_PATTERN = "\\$mutirpif\\(.+?\\)\\{.+?\\$endmutirpif\\}";
    public static final String MUTI_RP_IF_PATTERN_FOR_NAME = "\\$mutirpif\\(.+?\\)\\{";
    public static final String MUTI_RP_IF_PATTERN_FOR_NAME_START = "$mutirpif(";
    public static final String MUTI_RP_IF_PATTERN_FOR_NAME_END = "){";
    public static final String MUTI_RP_IF_PATTERN_END = "$endmutirpif}";

    /**
     * 用于文件夹的正则表达式
     */
    public static final String DIR_STRING_PATTERN = "\\$dir\\{\\{[\\S\\s]+?(}+)";
    public static final String DIR_STRING_PATTERN_START = "$dir{{";
    public static final String DIR_STRING_PATTERN_END = "}}";

    public static final String DIR_ARRAY_PATTERN = "(\\$dir-repeat\\(.+?\\{\\{).+?}}";
    public static final String DIR_ARRAY_PATTERN_FOR_NAME = "\\$dir-repeat\\(.+?\\)\\{\\{";
    public static final String DIR_ARRAY_PATTERN_FOR_NAME_START = "$dir-repeat(";
    public static final String DIR_ARRAY_PATTERN_FOR_NAME_END = "){{";

    public static final String FILE_STRING_PATTERN = "\\$file\\{\\{[\\S\\s]+?(}+)";
    public static final String FILE_STRING_PATTERN_START = "$file{{";
    public static final String FILE_STRING_PATTERN_END = "}}";

    public static final String FILE_ARRAY_PATTERN = "(\\$file-repeat\\(.+?\\{\\{).+?}}";
    public static final String FILE_ARRAY_PATTERN_FOR_NAME = "\\$file-repeat\\(.+?\\)\\{\\{";
    public static final String FILE_ARRAY_PATTERN_FOR_NAME_START = "$file-repeat(";
    public static final String FILE_ARRAY_PATTERN_FOR_NAME_END = "){{";

    public static final String FILE_ARRAY_PATTERN_FOR_ATTIBUTE = "\\$\\(.+?\\)";
    public static final String FILE_ARRAY_PATTERN_FOR_ATTIBUTE_START = "$(";
    public static final String FILE_ARRAY_PATTERN_FOR_ATTIBUTE_END = ")";

    //错误反馈表达式
    public static final String FORMAT_ERROR = "[errorForKey-%s]";
    public static final String FORMAT_ERROR_CONTENT = "[errorForContent-%s]";

}
