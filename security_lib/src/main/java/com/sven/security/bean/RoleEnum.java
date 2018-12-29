package com.sven.security.bean;

import java.util.Arrays;
import java.util.List;

public enum RoleEnum {
    /**
     *
     * UserDetailsService 需要的 Authority role 必须加 ROLE_ 前缀
     * 例如 : ROLE_ADMIN, ROLE_USER
     * 非常坑爹的设定
     *
    **/
    USER("USER"),
    ADMIN("ADMIN");
    private String value;
    public static final String ROLE_PREFIX = "ROLE_";
    RoleEnum(String value) {
        this.value = value;
    }

    public static List<RoleEnum> getAll(){
        return Arrays.asList(RoleEnum.class.getEnumConstants());
    }
    public static String[] getAllValues(){
        List<RoleEnum> roleEnums = getAll();
        String[] vals = new String[roleEnums.size()];
        for(int i = 0; i<roleEnums.size(); i++){
            vals[i] = roleEnums.get(i).getValue();
        }
        return vals;
    }

    public String getValue() {
        return value;
    }
}