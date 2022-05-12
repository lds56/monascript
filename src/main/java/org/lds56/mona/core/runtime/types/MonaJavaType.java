package org.lds56.mona.core.runtime.types;

import org.lds56.mona.core.exception.InvalidArgumentException;
import org.lds56.mona.core.exception.ContextAccessException;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class MonaJavaType extends MonaObject {

    // original value
    protected final Object value;

    // not null if cast to number
    private MonaNumber nvalue = null;

    // private Number numValue = null;

    @Override
    public boolean equals(Object x) {
        // MonaJ == Object
        if (this.value.equals(x)) return true;
        // MonaJ == MonaJ
        if (x instanceof MonaJavaType) return this.value.equals(((MonaJavaType)x).value);
        // super
        return super.equals(x);
    }

    public MonaJavaType(Object o) {
        value = o;
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.JavaType;
    }

    @Override
    public MonaNType getNType() {
        return getNValue().getNType();
    }

    private MonaNumber getNValue() {

        // return prepared value
        if (nvalue != null) {
            return nvalue;
        }

        if (value instanceof Number) {
            return MonaNumber.valueOf((Number) value);
        }

        return MonaNumber.NAN;
    }


    @Override
    public Object getValue() {
        return value;
    }

    public static MonaJavaType valueOf(Object o) {
        return new MonaJavaType(o);
    }

    @Override
    public boolean booleanValue() {
        if (value == null) return false;
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return true;
    }

    @Override
    public Number numberValue() {
        return getNValue().numberValue();
    }

    @Override
    public int intValue() {
        return getNValue().intValue();
    }

    @Override
    public long longValue() {
        return getNValue().longValue();
    }

    @Override
    public float floatValue() {
        return getNValue().floatValue();
    }

    @Override
    public double doubleValue() {
        return getNValue().doubleValue();
    }

    @Override
    public String stringValue() {
        return value.toString();
    }

    @Override
    public BigInteger bigIntValue() {
        return getNValue().bigIntValue();
    }

    @Override
    public BigDecimal bigDecValue() {
        return getNValue().bigDecValue();
    }

    /**
     * Access array or list element
     *
     * @param indexObject
     * @return
     */
    @Override
    public MonaObject getElement(final MonaObject indexObject) {

        final Object thisValue = getValue();
        final Object indexValue = indexObject.getValue();

        if (Objects.isNull(thisValue)) {
            throw new ContextAccessException("Cannot get element from null value");
        }

        if (Objects.isNull(indexValue)) {
            throw new ContextAccessException("Cannot use null index in collection");
        }

        Class<?> clazz = thisValue.getClass();

        Object result;
        if (Map.class.isAssignableFrom(clazz)) {
            result = ((Map) thisValue).get(indexValue);
        }
        else if (List.class.isAssignableFrom(clazz)) {
            if (indexValue instanceof Number) {
                result = ((List) thisValue).get(((Number) indexValue).intValue());
            } else {
                throw new InvalidArgumentException("Integer type expected when get element from list");
            }
        }
        else if (Set.class.isAssignableFrom(clazz)) {
            result = ((Set) thisValue).contains(indexValue)? indexValue : null;
        }
        else if (clazz.isArray()) {
            if (indexValue instanceof Number) {
                result = Array.get(thisValue, ((Number) indexValue).intValue());
            } else {
                throw new InvalidArgumentException("Integer type expected when get element from list");
            }
        }
        else {
            throw new ContextAccessException("Cannot get element from unsupported type, list or map expected");
        }

        return result != null ? MonaObject.wrap(result) : MonaNull.NIL;
    }

    // TODO: return mona object
    @Override
    public Iterator<?> iterator() {
        final Object value = getValue();
        if (Objects.isNull(value)) {
            throw new ContextAccessException("Cannot get an iterator from null value");
        }

        if (value.getClass().isArray()) {
            return Arrays.asList((Object[])value).iterator();
        }
        else if (value instanceof Collection) {
            return ((Collection) value).iterator();
        }
        else {
            throw new ContextAccessException("No iterable object");
        }
    }

    @Override
    public MonaIter iter() {
        return new MonaIter(iterator());
    }

    @Override
    public String toString() {
        return stringValue();
    }
}
