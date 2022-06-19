package org.lds56.mona.core.runtime.types;

/**
 * @Author: Rui Chen
 * @Date: 19 Jun 2022
 * @Description: This is description.
 */
public class MonaError extends MonaObject {

    private final int code;

    private final String msg;

    private final Exception ex;

    public MonaError(int code, String message, Exception ex) {
        this.msg = message;
        this.code = code;
        this.ex = ex;
    }

    public static MonaError err(int code, String message) {
        return new MonaError(code, message, null);
    }


    public static MonaError err(int code, String message, Exception ex) {
        return new MonaError(code, message, ex);
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Error;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Object getValue() {
        return ex;
    }
}
