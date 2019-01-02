package com.sven.common.lib.codetemplate.dataBean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class SourceFileInfo {
    private String name;
    private String path;
    private boolean leaf;
    private boolean isDir;

    /**
     * jackson 2 无法解析引用自身
     */
    @JsonIgnore
    private SourceFileInfo parent;
    private List<? extends SourceFileInfo> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<? extends SourceFileInfo> getChildren() {
        return children;
    }

    public void setChildren(List<? extends SourceFileInfo> children) {
        this.children = children;
    }

    public SourceFileInfo getParent() {
        return parent;
    }

    public void setParent(SourceFileInfo parent) {
        this.parent = parent;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    @Override
    public String toString() {
        return "SourceFileInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", leaf=" + leaf +
                ", isDir=" + isDir +
                ", children=" + children +
                '}';
    }
}
