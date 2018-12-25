package com.sven.common.lib.codetemplate.utils;

import java.io.File;

public class FileUtils {
    public static final String FILE_SEPARATION_DOT = ".";
    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(FILE_SEPARATION_DOT) != -1 && fileName.lastIndexOf(FILE_SEPARATION_DOT) != 0) {
            return fileName.substring(fileName.lastIndexOf(FILE_SEPARATION_DOT)+1);
        } else {
            return "";
        }
    }
}
