package com.sql.code.generator.modules.android.dataBean;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;

import java.util.Map;

/**
 * Created by sven-ou on 2017/2/22.
 */
public class ClassNameUtil {
    public static final ClassName RowMapper = ClassName.get("com.tts.android.db", "RowMapper");
    public static final ClassName DaoSupport = ClassName.get("com.tts.android.db", "DaoSupport");
    public static final ClassName Cursor = ClassName.get("android.database", "Cursor");
    public static final ClassName ContentValues = ClassName.get("android.content", "ContentValues");
    public static final ClassName DbUtils = ClassName.get("com.tts.android.db", "DbUtils");
    public static final ClassName TextUtils = ClassName.get("android.text", "TextUtils");
    public static final ClassName DateUtils = ClassName.get("com.tts.android.utils", "DateUtils");
    public static final ParameterizedTypeName MapStringObject = ParameterizedTypeName.get(Map.class, String.class, Object.class);
}
