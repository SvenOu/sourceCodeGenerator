package com.sven.common.lib.codetemplate.utils;

import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.TplSourceFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

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
    public static TplSourceFileInfo getSourceFileInfo(String path) {
        File parent = new File(path);
        if (!parent.exists()) {
            return null;
        }
        // for parent
        TplSourceFileInfo fileInfo = new TplSourceFileInfo();
        fileInfo.setDir(parent.isDirectory());
        fileInfo.setName(parent.getName());
        fileInfo.setOriginName(fileInfo.getName());
        fileInfo.setPath(parent.getAbsolutePath().replaceAll("\\\\", TPConfig.PATH_SEPARATOR));
        fileInfo.setOriginPath(fileInfo.getPath());
        // for children
        File[] childs = parent.listFiles();
        if (null == childs || childs.length <= 0) {
            fileInfo.setLeaf(true);
            return fileInfo;
        }
        sortFileChilds(childs);

        List<TplSourceFileInfo> cfiList = new ArrayList<>(childs.length);
        for (File c : childs) {
            if (!c.exists()) {
                continue;
            }
            TplSourceFileInfo cfi = getSourceFileInfo(c.getAbsolutePath());
            cfiList.add(cfi);
            cfi.setParent(fileInfo);
            fileInfo.setChildren(cfiList);
        }
        return fileInfo;
    }

    /**
     *
     * 根据文件夹类型排列数组
     */
    private static void sortFileChilds(File[] childs) {
        Comparator comp = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                File f1 = (File) o1;
                File f2 = (File) o2;
                if (f1.isDirectory() && !f2.isDirectory()) {
                    // Directory before non-directory
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    // Non-directory after directory
                    return 1;
                } else {
                    // Alphabetic order otherwise
                    return f1.compareTo(f2);
                }
            }
        };
        Arrays.sort(childs, comp);
    }
}
