package com.sven.common.lib.codetemplate.utils;

public class IdUtils {
    private static final String SEED = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String getId(String prefix) {
        StringBuilder t = new StringBuilder(prefix + System.currentTimeMillis());
        for(int i = 0; i < 4; ++i) {
            t.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt((int)(Math.random() * 26.0D)));
        }
        return t.toString();
    }
}