package org.lds56.mona.engine;

import org.lds56.mona.core.runtime.MonaGlobal;
import org.lds56.mona.core.script.ScriptComposer;
import org.lds56.mona.script.MonaExpression;
import org.lds56.mona.script.MonaScript;
import org.lds56.mona.utils.MD5Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author chenrui
 * @date 2021/11/23
 */
public class MonaEngine {

    public static final String NAME = "Mona";
    public static final String VERSION = "0.1.0";
    public static final String LANGUAGE = "MonaScript";
    public static final String LANGUAGE_VERSION = "0.1.0";

    public static EngineOptions engineMode;

    static {
        engineMode = EngineOptions
                        .setUp()
                        .setOption(EngineOption.IR_CODE_TRACE_STACK_PRINT_SWITCH, true)
                        .setOption(EngineOption.SCRIPT_EXECUTION_MODE, EngineOption.Choice.BYTECODE_SCRIPT);
    }

    // instance options
    private final EngineOptions options;

    // script cache
    private ConcurrentMap<String, MonaScript> scriptCache;

    // global
    private MonaGlobal monaGlobal;

    public MonaEngine() {
        this(EngineOptions.EMPTY);
    }

    // instance option
    public MonaEngine(EngineOptions options) {
        // engine options
        this.options = options;
        // script cache
        this.scriptCache = new ConcurrentHashMap<>();
        // global vars
        this.monaGlobal = new MonaGlobal();
    }

    // global mode
    public static EngineOptions getEngineMode() {
        return engineMode;
    }

    // script
    public MonaScript compile(String code) {
        return compile(code, false);
    }

    public MonaScript compile(String code, boolean useCache) {

        if (useCache) {
            String key = MD5Utils.encode(code);
            if (scriptCache.containsKey(key)) {
                return scriptCache.get(key);
            }
            MonaScript script = ScriptComposer.create(code, monaGlobal.getModule());
            scriptCache.put(key, script);
            return script;

        } else {
            return ScriptComposer.create(code, monaGlobal.getModule());
        }
    }

    public Object execute(String code) {
        MonaScript script = compile(code);
        return script.execute();
    }

    public Object execute(String code, Map<String, Object> context) {
        MonaScript script = compile(code);
        return script.execute(context);
    }

    // expression
    public Object evaluate(String expr, Map<String, Object> context) {
        return new MonaExpression(expr).evaluate(context);
    }
}
