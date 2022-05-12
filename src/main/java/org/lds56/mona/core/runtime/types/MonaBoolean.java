package org.lds56.mona.core.runtime.types;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Author: Rui Chen
 * @Date: 30 Apr 2022
 * @Description: Boolean Type 『真実はひとつ』
 */
public class MonaBoolean extends MonaObject {

    public static final MonaBoolean TRUE = new MonaBoolean(Boolean.TRUE);

    public static final MonaBoolean FALSE = new MonaBoolean(Boolean.FALSE);

    private Boolean value = null;

    public MonaBoolean(Boolean b) {
        value = b;
        // numValue = b? 1 : 0;
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Boolean;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.I;
    }

    @Override
    public Boolean getValue() {
        return value;
    }

    public static MonaBoolean valueOf(Boolean b) {
        return b? MonaBoolean.TRUE : MonaBoolean.FALSE;
    }

    public static MonaBoolean valueOf(boolean b) {
        return b? MonaBoolean.TRUE : MonaBoolean.FALSE;
    }

    @Override
    public boolean booleanValue() {
        return value;
    }

    @Override
    public Number numberValue() {
        return value? 1 : 0;
    }

    @Override
    public int intValue() {
        return value? 1 : 0;
    }

    @Override
    public long longValue() {
        return value? 1L : 0L;
    }

    @Override
    public float floatValue() {
        return value? 1f : 0;
    }

    @Override
    public double doubleValue() {
        return value? 1d: 0d;
    }

    @Override
    public String stringValue() {
        return value? "true" : "false";
    }

    @Override
    public BigInteger bigIntValue() {
        return BigInteger.valueOf(value? 1L : 0L);
    }

    @Override
    public BigDecimal bigDecValue() {
        return BigDecimal.valueOf(value? 1d: 0d);
    }


    @Override
    public String toString() {
        return value? "true" : "false";
    }
}
