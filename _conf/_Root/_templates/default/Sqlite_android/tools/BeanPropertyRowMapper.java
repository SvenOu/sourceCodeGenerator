package com.tts.android.mybatic.bean;

import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.tts.android.db.DbUtils;
import com.tts.android.db.RowMapper;
import com.tts.android.utils.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 基于反射机制
 * 筛选出同时具有getter和setter的field作为转换目标
 */
public class BeanPropertyRowMapper<T> extends BeanPropertyHolder<T> implements RowMapper<T> {
    private static final String TAG = BeanPropertyRowMapper.class.getSimpleName();

    public BeanPropertyRowMapper(Class<T> mappedClass) {
        super(mappedClass);
    }

    public static <T> BeanPropertyRowMapper<T> newInstance(Class<T> mappedClass) {
        return new BeanPropertyRowMapper<T>(mappedClass);
    }

    @Override
    public T mapRow(Cursor rs, int rowNumber) {
        Assert.state(this.mappedClass != null, "Mapped class was not specified");
        T mappedObject = null;
        try {
            mappedObject = this.mappedClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Field f : voFields) {
            invokeMethodForField(mappedObject, f, rs, rowNumber);
        }
        return mappedObject;
    }

    private void invokeMethodForField(T mappedObject, Field f, Cursor cursor, int rowNumber) {
        Class cls = f.getType();
        String databaseField = this.databaseFields.get(f.getName());
        Method setterMethod = this.setterMethods.get(f.getName());
        if (cls.getName().equals(byte.class.getName())
                || cls.getName().equals(short.class.getName())
                || cls.getName().equals(int.class.getName())
                || cls.getName().equals(long.class.getName())
                || cls.getName().equals(Byte.class.getName())
                || cls.getName().equals(Short.class.getName())
                || cls.getName().equals(Integer.class.getName())
                || cls.getName().equals(Long.class.getName())
                ) {
            String strValue = DbUtils.getCursorValue(cursor, databaseField, String.class);
            invokeMethod(setterMethod, mappedObject, TextUtils.isEmpty(strValue) ?
                    0 : Integer.valueOf(strValue));

        } else if (cls.getName().equals(float.class.getName()) ||
                cls.getName().equals(Float.class.getName())) {
            String strValue = DbUtils.getCursorValue(cursor, databaseField, String.class);
            invokeMethod(setterMethod, mappedObject, TextUtils.isEmpty(strValue) ?
                    0f : Float.valueOf(strValue));

        } else if (cls.getName().equals(double.class.getName())
                || cls.getName().equals(Double.class.getName())) {
            String strValue = DbUtils.getCursorValue(cursor, databaseField, String.class);
            invokeMethod(setterMethod, mappedObject, TextUtils.isEmpty(strValue) ?
                    0d : Float.valueOf(strValue));

        } else if (cls.getName().equals(boolean.class.getName())
                || cls.getName().equals(Boolean.class.getName())) {
            String strValue = DbUtils.getCursorValue(cursor, databaseField, String.class);
            invokeMethod(setterMethod, mappedObject, !(TextUtils.isEmpty(strValue) || "0".equals(strValue)));

        } else if (cls.getName().equals(Date.class.getName())) {
            invokeMethod(setterMethod, mappedObject, DbUtils.getCursorValue(cursor, databaseField, Date.class));
        } else {
            try {
                invokeMethod(setterMethod, mappedObject, DbUtils.getCursorValue(cursor, databaseField, cls));
            } catch (Exception e) {
                Log.w(TAG, e.getMessage());
            }
        }
    }
}
