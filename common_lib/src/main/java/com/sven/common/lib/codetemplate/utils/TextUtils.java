package com.sven.common.lib.codetemplate.utils;
import java.util.regex.Pattern;
public final class TextUtils {
    private static final String UNDERSCORE_CHAR = "_";
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d");

    public static String underscoreName(String name) {
        if (name == null || "".equals(name)) {
            return "";
        }
        else {
            StringBuilder result = new StringBuilder();
            result.append(name.substring(0, 1).toLowerCase());

            for (int i = 1; i < name.length(); ++i) {
                String s = name.substring(i, i + 1);
                String slc = s.toLowerCase();
                if (!isDigit(slc) && !s.equals(slc)) {
                    result.append(UNDERSCORE_CHAR).append(slc);
                }
                else {
                    result.append(s);
                }
            }

            return result.toString();
        }
    }

    public static String camelCaseName(String name) {
        StringBuffer result = new StringBuffer();
        if (name != null && name.length() > 0) {
            result.append(name.substring(0, 1).toLowerCase());
            for (int camelCaseName = 1; camelCaseName < name.length(); ++camelCaseName) {
                String s = name.substring(camelCaseName, camelCaseName + 1);
                if (UNDERSCORE_CHAR.equals(s)) {
                    s = name.substring(camelCaseName + 1, camelCaseName + 2).toUpperCase();
                    ++camelCaseName;
                }

                result.append(s);
            }
        }

        String var4 = result.toString();
        return var4;
    }

    public static boolean isDigit(String s) {
        return DIGIT_PATTERN.matcher(s).matches();
    }
}