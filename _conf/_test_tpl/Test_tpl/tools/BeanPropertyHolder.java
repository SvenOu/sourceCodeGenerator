package com.tts.android.mybatic.bean;

import android.text.TextUtils;
import android.util.Log;

import com.tts.android.utils.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于反射机制
 * 筛选出同时具有getter和setter的field作为转换目标
 */

public class BeanPropertyHolder<T> {
    private static final String TAG = BeanPropertyRowMapper.class.getSimpleName();

    protected static final String UNDERSCORE_CHAR = "_";
    protected static final String SETTER_METHOD_PREFIX = "set";
    protected static final String GETTER_METHOD_PREFIX = "get";
    protected static final String GETTER_METHOD_PREFIX_FOR_BOOLEAN = "is";
    protected T target;
    protected Class<T> mappedClass;
    protected List<Field> voFields;
    protected Map<String, String> databaseFields;
    protected Map<String, Method> getterMethods;
    protected Map<String, Method> setterMethods;

    private static final int FIND_METHOD = 0;
    private static final int VO_FIELDS = 1;
    private static final int DATABASE_FIELDS = 2;
    private static final int GETTER_METHODS = 3;
    private static final int SETTER_METHODS = 4;

    public BeanPropertyHolder() {}

    public BeanPropertyHolder(T t) {
        initializeFoeObject(t);
    }

    public BeanPropertyHolder(Class<T> mappedClass) {
        initializeForClass(mappedClass);
    }

    protected void initializeFoeObject(T t) {
        this.target = t;
        initializeForClass((Class<T>) this.target.getClass());
    }

    protected void initializeForClass(Class<T> mappedClass) {
        this.mappedClass = mappedClass;
        //优先获取cache
        String voFieldsKey = generateInitKey(VO_FIELDS, mappedClass);
        String databaseFieldsKey = generateInitKey(DATABASE_FIELDS, mappedClass);
        String getterMethodsKey = generateInitKey(GETTER_METHODS, mappedClass);
        String setterMethodsKey = generateInitKey(SETTER_METHODS, mappedClass);
        BeanPropertyCache cache = BeanPropertyCache.getInstance();
        if(null != cache.getCache(voFieldsKey)
                && null != cache.getCache(databaseFieldsKey)
                && null != cache.getCache(getterMethodsKey)
                && null != cache.getCache(setterMethodsKey)){
            this.voFields = (List<Field>) cache.getCache(voFieldsKey);
            this.databaseFields = (Map<String, String>) cache.getCache(databaseFieldsKey);
            this.getterMethods = (Map<String, Method>) cache.getCache(getterMethodsKey);
            this.setterMethods = (Map<String, Method>) cache.getCache(setterMethodsKey);
            return;
        }

        this.voFields = new ArrayList<>();
        this.databaseFields = new HashMap<>();
        this.getterMethods = new HashMap<>();
        this.setterMethods = new HashMap<>();
        Field[] allFields = this.mappedClass.getDeclaredFields();
        for (Field field : allFields) {
            String setterMethodName = SETTER_METHOD_PREFIX + upCaseFirst(field.getName());
            Method setterMethod = findMethod(this.mappedClass, setterMethodName, field.getType());
            if (null != setterMethod) {
                this.setterMethods.put(field.getName(), setterMethod);
            }
            String getterMethodName = GETTER_METHOD_PREFIX + upCaseFirst(field.getName());
            Method getterMethod = findMethod(this.mappedClass, getterMethodName);
            if (null != getterMethod) {
                this.getterMethods.put(field.getName(), getterMethod);
            } else {
                if (field.getType().getName().equals(boolean.class.getName())
                        || field.getType().getName().equals(Boolean.class.getName())) {
                    getterMethodName = GETTER_METHOD_PREFIX_FOR_BOOLEAN + upCaseFirst(field.getName());
                    getterMethod = findMethod(this.mappedClass, getterMethodName);
                    if (null != getterMethod) {
                        this.getterMethods.put(field.getName(), getterMethod);
                    }
                }
            }
            // 筛选有 setter 和 getter 的 field
            if (null != setterMethod && null != getterMethod) {
                if(null == field.getAnnotation(DisabledForSql.class)){
                    this.voFields.add(field);
                    this.databaseFields.put(field.getName(), underscoreName(field.getName()));
                }
            }
        }
        cache.cache(voFieldsKey, this.voFields);
        cache.cache(databaseFieldsKey, this.databaseFields);
        cache.cache(getterMethodsKey, this.getterMethods);
        cache.cache(setterMethodsKey, this.setterMethods);
    }

    protected String upCaseFirst(String name) {
        if (!TextUtils.isEmpty(name)) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }

    protected String underscoreName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase());
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase();
            if (!s.equals(slc)) {
                result.append(UNDERSCORE_CHAR).append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    protected Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Method m = (Method) BeanPropertyCache.getInstance()
                .getCache(generateFindMethodKey(clazz, name,  paramTypes));
        if(null != m){
            return m;
        }
        Class<?> searchType = clazz;
        while (searchType != null) {
            Method[] methods = (searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods());
            for (Method method : methods) {
                if (name.equals(method.getName()) &&
                        (paramTypes == null || Arrays.equals(paramTypes, method.getParameterTypes()))) {
                    BeanPropertyCache.getInstance().cache(generateFindMethodKey(clazz, name,  paramTypes), method);
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }
    protected static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        }
        catch (Exception ex) {
            Log.e(TAG, ex.getMessage(), ex);
        }
        throw new IllegalStateException("Should never get here");
    }

    private  String generateInitKey(int type, Class<?> mappedClass) {
        return type + mappedClass.getName();
    }

    private String generateFindMethodKey(Class<?> clazz, String name, Class<?>[] paramTypes) {
        String paramTypesStr = "";
        for (Class<?> pt : paramTypes) {
            paramTypesStr = paramTypesStr + pt.getName();
        }
        return FIND_METHOD + clazz.getName() + name + paramTypesStr;
    }


    public Class<T> getMappedClass() {
        return mappedClass;
    }

    public List<Field> getVoFields() {
        return voFields;
    }

    public Map<String, String> getDatabaseFields() {
        return databaseFields;
    }

    public Map<String, Method> getGetterMethods() {
        return getterMethods;
    }

    public Map<String, Method> getSetterMethods() {
        return setterMethods;
    }
}
