package com.sven.common.lib.codetemplate.engine;
import com.sven.common.lib.codetemplate.config.TPConfig;
import com.sven.common.lib.codetemplate.dataBean.TplSourceFileInfo;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileGenerator {
    public void generateTempTpls(Map rootData, String rootPath, String tempRootPath, TplSourceFileInfo rootInfo) throws IOException {
        modifySourceDirsData(rootData, rootPath, rootInfo, tempRootPath);
        createSourceFiles(rootData, rootPath, rootInfo, tempRootPath);
    }

    private void modifySourceDirsData(Map rootData, String rootPath, TplSourceFileInfo info, String generateRootPath){
        if (info.isDir()) {
            if (Pattern.compile(TPConfig.DIR_STRING_PATTERN, Pattern.DOTALL).matcher(info.getName()).find()) {
                modifyDirsFromModel(rootData, rootPath, info, generateRootPath);
            }
        }
        List<TplSourceFileInfo> childs = (List<TplSourceFileInfo>) info.getChildren();
        if (null != childs) {
            for (int i = 0; i < childs.size(); i++) {
                modifySourceDirsData(rootData, rootPath, childs.get(i), generateRootPath);
            }
        }
    }

    private void modifyDirsFromModel(Map rootData, String rootPath, TplSourceFileInfo info, String generateRootPath) {
        StringBuffer sb = new StringBuffer();
        Pattern pat = Pattern.compile(TPConfig.DIR_STRING_PATTERN, Pattern.DOTALL);
        Matcher matcher = pat.matcher(info.getName());
        while (matcher.find()) {
            String s = matcher.group();
            String key = s.substring(6, s.length() - 2);

            int formatIndex = key.indexOf(TPConfig.FORMAT_SEPARATE_CHAR);
            String formatType = null;
            if (formatIndex > 0) {
                formatType = key.substring(formatIndex + 1);
                key = key.substring(0, formatIndex);
            }
            String value = StringUtils.isEmpty(rootData.get(key)) ? "" : (String) rootData.get(key);
            if (formatType != null) {
                value = CaseFormat.formatString(value, formatType);
            }
            matcher.appendReplacement(sb, value);
        }
        matcher.appendTail(sb);
        String parentDirPath = info.getPath().replace(info.getName(), "");
        String newParentDirPath = parentDirPath.replace(rootPath, generateRootPath);
        TplSourceFileInfo ci = info.cloneSelf();
        ci.setName(sb.toString());
        ci.setPath(newParentDirPath + sb);
        modifyChilds(ci);
    }

    private void modifyChilds(TplSourceFileInfo info) {
        List<TplSourceFileInfo> childs = (List<TplSourceFileInfo>) info.getChildren();
        if (null == childs) {
            return;
        }
        for (TplSourceFileInfo c : childs) {
            //modify parent, path
            c.setPath(info.getPath() + TPConfig.PATH_SEPARATOR + c.getName());
            c.setParent(info);
            modifyChilds(c);
        }
    }

    private void createSourceFiles(Map rootData, String rootPath, TplSourceFileInfo info, String generateRootPath) throws IOException {
        if (info.isLeaf()) {
            String parentDirPath = info.getPath().replace(info.getName(), "");
            String newFileParentDirPath = parentDirPath.replace(rootPath, generateRootPath);
            File newDir = new File(newFileParentDirPath);
            if (!newDir.exists()) {
                newDir.mkdirs();
            }
            String newFilePath = newFileParentDirPath + info.getName();
            File newFile = new File(newFilePath);
            if (info.isDir()) {
                newFile.mkdir();
            } else { // is file
                FileCopyUtils.copy(new File(info.getOriginPath()), newFile);
            }
        }
        List<TplSourceFileInfo> childs = (List<TplSourceFileInfo>) info.getChildren();
        if (null != childs) {
            for (TplSourceFileInfo c : childs) {
                createSourceFiles(rootData, rootPath, c, generateRootPath);
            }
        }
    }
}
