package com.tts.android.mybatic.bean;

import java.util.HashMap;
import java.util.Map;

// 此缓存基于不在运行时修改class的前提，使用反射api 在运行时改变过的的class将会出错
public class BeanPropertyCache {

    private static class HolderCls {
        private static final BeanPropertyCache instance = new BeanPropertyCache();
    }

    private BeanPropertyCache() {
    }

    public static BeanPropertyCache getInstance() {
        return HolderCls.instance;
    }

    private static Map cache = new HashMap();

    public Map getCache() {
        return cache;
    }

    public Object getCache(String key) {
        return cache.get(key);
    }

    public synchronized void cache(String key, Object object) {
        cache.put(key, object);
    }
}
