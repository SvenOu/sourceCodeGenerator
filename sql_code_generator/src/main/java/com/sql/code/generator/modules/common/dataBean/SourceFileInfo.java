package com.sql.code.generator.modules.common.dataBean;

import java.util.List;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class SourceFileInfo {
    private String name;
    private String path;
    private boolean leaf;
    private SourceFileInfo parent;
    private List<SourceFileInfo> children;

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

    public List<SourceFileInfo> getChildren() {
        return children;
    }

    public void setChildren(List<SourceFileInfo> children) {
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

    @Override
    public String toString() {
        return "SourceFileInfo{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", leaf=" + leaf +
                ", parent=" + parent +
                ", children=" + children +
                '}';
    }
}
