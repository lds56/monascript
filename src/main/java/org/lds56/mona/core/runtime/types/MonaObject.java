package org.lds56.mona.core.runtime.types;

// import org.lds56.mona.core.compiler.method.MethodFactory;
import org.lds56.mona.core.exception.ContextAccessException;
import org.lds56.mona.core.exception.InvokeErrorException;
import org.lds56.mona.core.exception.TypeBadCastException;
// import org.apache.commons.beanutils.PropertyUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;

public abstract class MonaObject implements Serializable {

    private static final long serialVersionUID = -6006961429175160222L;

    public abstract MonaType getMonaType();

    public abstract MonaNType getNType();

    public abstract Object getValue();

    public boolean booleanValue() {
        throw new TypeBadCastException("no boolean value");
    }

    public Number numberValue() {
        throw new TypeBadCastException("no number value");
    }

    public String stringValue() {
        throw new TypeBadCastException("no string value");
    }

    public int intValue() {
        throw new TypeBadCastException("no int value");
    }

    public long longValue() {
        throw new TypeBadCastException("no long value");
    }

    public float floatValue() {
        throw new TypeBadCastException("no float value");
    }

    public double doubleValue() {
        throw new TypeBadCastException("no double value");
    }

    public BigInteger bigIntValue() {
        throw new TypeBadCastException("no big integer value");
    }

    public BigDecimal bigDecValue() {
        throw new TypeBadCastException("no big decimal value");
    }

    public boolean ternaryCondition() {
        if (MonaType.isUndefined(this)) {
            return false;
        }
        return booleanValue();
    }

    /**
     * Access array, list or map element
     */
    public MonaObject getElement(final MonaObject indexObject) {
        throw new ContextAccessException("Not a list or map");
    }

    public Iterator<?> iterator() {
        throw new ContextAccessException("Not a iterator collection");
    }


    public MonaIter iter() {
        throw new ContextAccessException("Not a iter collection");
    }
//
//    public MonaObject getProperty(String propName) {
//        try {
//            return wrap(PropertyUtils.getProperty(this.getValue(), propName));
//        } catch (NoSuchMethodException e) {
//            return MonaUndefined.UNDEF;
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            throw new ContextAccessException("No such a property: " + propName, e);
//        }
//    }
//
//    public MonaObject invokeMethod(String methodName, Object[] args) {
//        try {
//            return wrap(MethodFactory.invokeByName(this.getValue(), methodName, args));
//        } catch (FunctionNotFoundException e) {
//            return MonaUndefined.UNDEF;
//        }
////        } catch (NoSuchMethodException e) {
////            return MonaUndefined.UNDEF;
////        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
////            throw new ExpressionRuntimeException("No such a method: " + methodName, e);
////        }
//    }

    public MonaObject invoke(MonaObject[] args) {
        throw new InvokeErrorException("Not a callable mona object");
    }

    public static MonaObject wrap(Object o) {
        // mona x
        if (o == null) {
            return MonaNull.NIL;
        }
        // mona o
        if (o instanceof MonaObject) {
            return (MonaObject) o;
        }
        // mona n
        if (o instanceof Number) {
            return MonaNumber.valueOf((Number)o);
        }
        // mona s
        if (o instanceof String || o instanceof Character) {
            return MonaString.valueOf(o.toString());
        }
        // mona b
        if (o instanceof Boolean) {
            return MonaBoolean.valueOf((Boolean)o);
        }
        return MonaJavaType.valueOf(o);
    }

    @Override
    public String toString() {
        return "MonaObject|" + this.getClass().getSimpleName();
    }
}
