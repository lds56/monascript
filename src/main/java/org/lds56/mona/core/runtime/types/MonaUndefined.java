package org.lds56.mona.core.runtime.types;

import org.lds56.mona.core.exception.ContextAccessException;
import org.lds56.mona.core.exception.TypeBadCastException;

public class MonaUndefined extends MonaNull {

    public static final MonaUndefined UNDEF = new MonaUndefined();

    private MonaUndefined() {}

    @Override
    public MonaType getMonaType() {
        return MonaType.Undefined;
    }

    @Override
    public Object getValue() {
        throw new ContextAccessException("Cannot get value from `Undefined`");
    }

    @Override
    public boolean booleanValue() {
        throw new TypeBadCastException("Undefined is not a boolean");
    }

    @Override
    public Number numberValue() {
        throw new TypeBadCastException("Undefined is not a number");
    }

    @Override
    public int intValue() {
        throw new TypeBadCastException("Undefined is not a integer");
    }

    @Override
    public long longValue() {
        throw new TypeBadCastException("Undefined is not a long");
    }

    @Override
    public float floatValue() {
        throw new TypeBadCastException("Undefined is not a float");
    }

    @Override
    public double doubleValue() {
        throw new TypeBadCastException("Undefined is not a double");
    }

    @Override
    public String stringValue() {
        throw new TypeBadCastException("Undefined is not a string");
    }

//    @Override
//    public MonaObject getProperty(String propName) {
//        return MonaUndefined.UNDEF;
//    }
//
//    @Override
//    public MonaObject invokeMethod(String methodName, Object[] args) {
//        return MonaUndefined.UNDEF;
//    }

    @Override
    public String toString() {
        return "Undefined";
    }
}
