package com.sql.code.generator.modules.common.composite;

public class ClassTypeUtils {
    public static boolean isPrimitiveWrapper(String type) {
        switch (type){
            case "byte": return true;
            case "short": return true;
            case "int": return true;
            case "long": return true;
            case "float": return true;
            case "double": return true;
            case "boolean": return true;
            case "char": return true;
            default: return false;
        }
    }
}
