package org.lds56.mona.core.runtime.types;

import org.lds56.mona.core.exception.TypeBadCastException;

public class MonaNull extends MonaObject {

    public static final MonaNull NIL = new MonaNull();

    public MonaNull() {}

    @Override
    public MonaType getMonaType() {
        return MonaType.Null;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public boolean booleanValue() {
        return false;
    }

    @Override
    public Number numberValue() {
        throw new TypeBadCastException("Null is not a number");
    }

    @Override
    public int intValue() {
        throw new TypeBadCastException("Null is not a integer");
    }

    @Override
    public long longValue() {
        throw new TypeBadCastException("Null is not a long");
    }

    @Override
    public float floatValue() {
        throw new TypeBadCastException("Null is not a float");
    }

    @Override
    public double doubleValue() {
        throw new TypeBadCastException("Null is not a double");
    }

    @Override
    public String stringValue() {
        throw new TypeBadCastException("Null is not a string");
    }

    @Override
    public String toString() {
        return "Nil";
    }

}
