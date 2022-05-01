package org.lds56.mona.core.runtime.types;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Mona types
 *
 * Created by  flyer on 2020-08-28
 *
 */
public enum MonaType implements Serializable {

    Number,
    Boolean,
    String,
    JavaType,
    Collection,
    Null,
    Undefined,
    Method,
    Function,
    Lambda,
    Tuple
    ;

    public static boolean isNull(MonaObject a) {
        return a.getMonaType() == MonaType.Null || a.getMonaType() == MonaType.JavaType && a.getValue() == null;
    }

    public static boolean isUndefined(MonaObject a) {
        return a.getMonaType() == MonaType.Undefined;
    }

    public static boolean isString(MonaObject a) {
        return a.getMonaType() == MonaType.String ||
               a.getMonaType() == MonaType.JavaType &&  (a.getValue() instanceof String || a.getValue() instanceof Character);
    }

    public static boolean isNumber(MonaObject a) {
        return a.getMonaType() == MonaType.Number ||
               a.getMonaType() == MonaType.JavaType && a.getValue() instanceof Number;
    }

    public static boolean isBigInteger(MonaObject a) {
        return a.getMonaType() == MonaType.Number && a.getNType() == MonaNType.BI ||
               a.getMonaType() == MonaType.JavaType && a.getValue() instanceof BigInteger;
    }

    public static boolean isBigDecimal(MonaObject a) {
        return a.getMonaType() == MonaType.Number && a.getNType() == MonaNType.BD ||
               a.getMonaType() == MonaType.JavaType && a.getValue() instanceof BigDecimal;
    }

    public static boolean isBoolean(MonaObject a) {
        return a.getMonaType() == MonaType.Boolean ||
               a.getMonaType() == MonaType.JavaType && a.getValue() instanceof Boolean;
    }

}
