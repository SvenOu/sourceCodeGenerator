package com.sven.common.lib.codetemplate.utils;

import com.sven.common.lib.codetemplate.config.TPConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Map;

public class JsScriptUtils {
    private static Log log = LogFactory.getLog(JsScriptUtils.class);
    private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
    public static Boolean runBooleanScript(String jsExcuteble, Map context, Map rootContext){
        bindRootContext(engine, rootContext);
        bindContext(engine, context);
        Object val;
        synchronized(JsScriptUtils.class) {
            try {
                engine.getBindings(ScriptContext.ENGINE_SCOPE).clear();
                val = engine.eval(jsExcuteble);
            } catch (ScriptException e) {
//                e.printStackTrace();
                log.warn(e.getMessage());
                return false;
            }
        }
        if(val == null){
            return false;
        }
        if(val instanceof String && !StringUtils.isEmpty(val)){
            return true;
        }
        else {
            return (Boolean) val;
        }
    }

    private static void bindRootContext(ScriptEngine engine, Map context) {
        if (context == null) {
            throw new NullPointerException("context map is null");
        }
        bindContext(TPConfig.STRING_ROOT_SCOPE, engine, context);
    }

    private static void bindContext(ScriptEngine engine, Map<? extends String, ? extends Object> context) {
        if (context == null) {
            throw new NullPointerException("context map is null");
        }
        bindContext("", engine, context);
    }

    private static void bindContext(String prefix, ScriptEngine engine, Map<? extends String, ? extends Object> context) {
        if (context == null) {
            return;
        }
        for (Map.Entry<? extends String, ? extends Object> entry : context.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();
            String newKey = StringUtils.isEmpty(prefix) ? key: prefix + "." + key;
            engine.put(newKey, val);
            if(val instanceof Map){
                bindContext(newKey, engine, (Map<? extends String, ? extends Object>) val);
            }
        }
    }
}
