package org.lds56.mona.core.runtime.types;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MonaString extends MonaObject {

    // constants
    public static final MonaString EMPTY = new MonaString("");

    // original value
    private final String value;

    // not null if cast to number
    private MonaNumber nvalue = null;

    public MonaString(String s) {
        value = s;
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.String;
    }

    @Override
    public MonaNType getNType() {
        return getNValue().getNType();
    }

    @Override
    public String getValue() {
        return value;
    }

    public static MonaString valueOf(String s) {
        return new MonaString(s);
    }

    private MonaNumber getNValue() {

        // return prepared value
        if (nvalue != null) {
            return nvalue;
        }

        // empty string
        if ("".equals(value)) {
            return MonaNumber.valueOf(0, MonaNType.I);
        }

        // real number
        if (value.contains(".")) {
            try {
                double d = Double.parseDouble(value);
                return MonaNumber.valueOf(d, MonaNType.D);
            } catch (NumberFormatException e2) {
                return MonaNumber.NAN;
            }
        }

        // natrual number
        try {
            int i = Integer.parseInt(value);
            return MonaNumber.valueOf(i, MonaNType.I);
        } catch (NumberFormatException e1) {
            try {
                long l = Long.parseLong(value);
                return MonaNumber.valueOf(l, MonaNType.L);
            } catch (NumberFormatException e2) {
                return MonaNumber.NAN;
            }
        }

    }

    @Override
    public boolean booleanValue() {
        return !value.isEmpty() && !"false".equals(value);
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
        return value;
    }

    @Override
    public BigInteger bigIntValue() {
        return "".equals(value)? BigInteger.ZERO : new BigInteger(value);
    }

    @Override
    public BigDecimal bigDecValue() {
        return "".equals(value)? BigDecimal.ZERO : new BigDecimal(value);
    }

    @Override
    public String toString() {
        return stringValue();
    }
}
