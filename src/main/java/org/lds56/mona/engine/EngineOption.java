package org.lds56.mona.engine;

public enum EngineOption {

    SCRIPT_CACHE_SIZE(Type.SIZE, 1000),

    COMPILE_THREAD_POOL_SIZE(Type.SIZE, 2),

    SOURCE_CODE_PRINT_SWITCH(Type.SWITCH, false),

    IR_CODE_TRACE_STACK_PRINT_SWITCH(Type.SWITCH, false),

    SCRIPT_EXECUTION_MODE(Type.CHOICE, Choice.BYTECODE_SCRIPT),

    ;

    public enum Type {
        SWITCH,
        LEVEL,
        SIZE,
        CHOICE
    }

    public enum Choice {

        BYTECODE_SCRIPT,

        EVAL_EXPRESSION,
    }

    private final Type type;
    private final Object defaultValue;

    EngineOption(Type type, Object defaultValue) {
        this.type = type;
        this.defaultValue = defaultValue;
    }

    Object getDefault() {
        return defaultValue;
    }

    Type getType() {
        return type;
    }
}
