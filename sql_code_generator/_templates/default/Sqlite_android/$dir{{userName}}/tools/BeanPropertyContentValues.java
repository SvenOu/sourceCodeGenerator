package com.tts.android.mybatic.bean;

import android.content.ContentValues;

import com.tts.android.utils.DateUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sven-ou on 2017/3/1.
 */

/**
 * 基于反射机制
 * 筛选出同时具有getter和setter的field作为转换目标
 */
public class BeanPropertyContentValues<T> extends BeanPropertyHolder<T> {
    private static final String TAG = BeanPropertyRowMapper.class.getSimpleName();

    public BeanPropertyContentValues(T object) {
        initializeFoeObject(object);
    }

    public static <T> ContentValues newInstance(T object) {
        ContentValues cvs = null;
        try {
            cvs = new BeanPropertyContentValues(object).generateContentValues();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cvs;
    }

    public ContentValues generateContentValues() {
        ContentValues v = new ContentValues();
        for (Field f : this.voFields) {
            String databaseField = this.databaseFields.get(f.getName());
            Method getterMethod = this.getterMethods.get(f.getName());
            String value;
//            if (f.getType().getName().equals(Date.class.getName())) {
//                Date d = (Date) invokeMethod(getterMethod, this.target);
//                value = DateUtils.isoFormatter(d);
//            } else {
//                value = (String) invokeMethod(getterMethod, this.target);
//            }
            value = invokeMethodAndConvertString(getterMethod, this.target);
            v.put(databaseField, value);
        }
        return v;
    }

    private String invokeMethodAndConvertString(Method getterMethod, T target) {
        Object obj = invokeMethod(getterMethod, target);
        if (null == obj) {
            return null;
        }
        Class<? extends Object> cls = obj.getClass();
        if (cls.getName().equals(byte.class.getName())
                || cls.getName().equals(short.class.getName())
                || cls.getName().equals(int.class.getName())
                || cls.getName().equals(long.class.getName())
                || cls.getName().equals(Byte.class.getName())
                || cls.getName().equals(Short.class.getName())
                || cls.getName().equals(Integer.class.getName())
                || cls.getName().equals(Long.class.getName())
                ) {
            return String.valueOf(obj);
        } else if (cls.getName().equals(float.class.getName())
                || cls.getName().equals(Float.class.getName())
                ) {
            return String.valueOf(obj);
        } else if (cls.getName().equals(double.class.getName())
                || cls.getName().equals(Double.class.getName())
                ) {
            return String.valueOf(obj);

        } else if (cls.getName().equals(boolean.class.getName())
                || cls.getName().equals(Boolean.class.getName())
                ) {
            Boolean r = (Boolean) obj;
            return r ? "1" : "0";
        } else if (cls.getName().equals(Date.class.getName())) {
            Date d = (Date) invokeMethod(getterMethod, target);
            return DateUtils.isoFormatter(d);
        } else {
            return String.valueOf(obj);
        }
    }

    public Map<String, String> generateMap() {
        Map<String, String> v = new HashMap<>();
        for (Field f : this.voFields) {
            String databaseField = this.databaseFields.get(f.getName());
            Method getterMethod = this.getterMethods.get(f.getName());
            String value;
//            if (f.getType().getName().equals(Date.class.getName())) {
//                Date d = (Date) invokeMethod(getterMethod, this.target);
//                value = DateUtils.isoFormatter(d);
//            } else {
//                value = (String) invokeMethod(getterMethod, this.target);
//            }
            value = invokeMethodAndConvertString(getterMethod, this.target);
            v.put(databaseField, value);
        }
        return v;
    }
}
