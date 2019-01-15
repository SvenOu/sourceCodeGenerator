package com.sven.common.lib.codetemplate.dataBean;

import org.springframework.beans.BeanUtils;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class TplSourceFileInfo extends SourceFileInfo{
    private String originName;
    private String originPath;

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getOriginPath() {
        return originPath;
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    @Override
    public String toString() {
        return "TplSourceFileInfo{" +
                "originName='" + originName + '\'' +
                ", originPath='" + originPath + '\'' +
                '}';
    }
}
