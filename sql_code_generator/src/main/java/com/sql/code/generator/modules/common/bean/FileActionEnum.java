package com.sql.code.generator.modules.common.bean;

import java.util.Arrays;
import java.util.List;

public enum FileActionEnum {
    /**
     *
     */
    EDIT_NAME("edit name"),
    NEW_FOLDER("new folder"),
    NEW_FILE("new file"),
    NEW_CHILD_FOLDER("new child folder"),
    NEW_CHILD_FILE("new child file"),
    DELETE("delete");

    private String name;

    FileActionEnum(String name) {
        this.name = name;
    }

    public static List<FileActionEnum> getAll(){
        return Arrays.asList(FileActionEnum.class.getEnumConstants());
    }

    public static String[] getAllValues(){
        List<FileActionEnum> roleEnums = getAll();
        String[] vals = new String[roleEnums.size()];
        for(int i = 0; i<roleEnums.size(); i++){
            vals[i] = roleEnums.get(i).getName();
        }
        return vals;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}