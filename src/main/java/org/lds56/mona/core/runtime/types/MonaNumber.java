package org.lds56.mona.core.runtime.types;

import org.lds56.mona.core.exception.TypeBadCastException;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MonaNumber extends MonaObject {

    // private static final double MAGIC_DOUBLE = -12345678d;
    public static final MonaNumber NAN = new MonaNumber(Double.NaN, MonaNType.E);

    protected Number value = null;

    protected long integralValue;

    protected double realValue;

    protected MonaNType ntype;

    // construct from a Number
    public MonaNumber(Number n) {
        this.value = n;
        this.ntype = MonaNType.map2ntype(n);
        if (this.ntype.isIntegral()) {
            this.integralValue = n.longValue();
        } else {
            this.realValue = n.doubleValue();
        }
    }

    // constuct from bignumber string
    public MonaNumber(String bigNumber, MonaNType ntype) {
        if (ntype == MonaNType.BI) {
            this.value = new BigInteger(bigNumber);
            this.integralValue = this.value.longValue();
        }
        else if (ntype == MonaNType.BD) {
            this.value = new BigDecimal(bigNumber);
            this.realValue = this.value.doubleValue();
        }
        else {
            throw new TypeBadCastException("Big Number ntype expected for this constructor");
        }
        this.ntype = ntype;
    }

    // construct from Number with ntype
    public MonaNumber(Number n, MonaNType ntype) {
        this.value = n;
        this.ntype = ntype;
        if (this.ntype.isIntegral()) {
            this.integralValue = n.longValue();
        } else {
            this.realValue = n.doubleValue();
        }
    }

    // construct with double with ntype
    public MonaNumber(double d, MonaNType ntype) {
        // lazy eval
        // this.value = ntype.newNumber(d);
        this.realValue = d;
        this.ntype = ntype;
    }

//    public MonaNumber(Number n, double d, MonaNType ntype) {
//        this.value = n;
//        this.realValue = d;
//        this.ntype = ntype;
//    }

    public MonaNumber(long l, MonaNType ntype) {
        // lazy eval
        // this.value = ntype.newNumber(d);
        this.integralValue = l;
        this.ntype = ntype;
    }

//    public MonaNumber(Number n, long l, MonaNType ntype) {
//        this.value = n;
//        this.integralValue = l;
//        this.ntype = ntype;
//    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Number;
    }

    @Override
    public Number getValue() {
        return numberValue();
    }

    @Override
    public MonaNType getNType() {
        return ntype;
    }

    public static MonaNumber valueOf(Number n) {
        return new MonaNumber(n);
    }


    public static MonaNumber newLong(long l) {
        return new MonaNumber(l, MonaNType.L);
    }

    public static MonaNumber newInteger(long l) {
        return new MonaNumber(l, MonaNType.I);
    }

    public static MonaNumber newDouble(double d) {
        return new MonaNumber(d, MonaNType.D);
    }

    public static MonaNumber newFloat(double d) {
        return new MonaNumber(d, MonaNType.F);
    }

    public static MonaNumber valueOf(String s, MonaNType ntype) {
        return new MonaNumber(s, ntype);
    }

    public static MonaNumber valueOf(Number n, MonaNType ntype) {
        return new MonaNumber(n, ntype);
    }

    public static MonaNumber valueOf(double d, MonaNType ntype) {
        // should be ? return new MonaNumber(d, MonaNType.lower(ntype, MonaNType.D));
        return new MonaNumber(d, ntype);
    }

    public static MonaNumber valueOf(long l, MonaNType ntype) {
        return new MonaNumber(l, MonaNType.lower(ntype, MonaNType.L));
    }

//    public static <T extends Number> MonaN<T> valueOf(double d, Class<T> clz) throws Exception {
//        return new MonaN<T>(clz.getConstructor(double.class).newInstance(d));
//    }

    @Override
    public boolean booleanValue() {
        return ntype.isIntegral()? integralValue != 0 :
            realValue != 0 && !Double.isNaN(realValue);
    }

    @Override
    public Number numberValue() {
        if (MonaNType.E.equals(ntype)) {
            throw new TypeBadCastException("Not a valid number type");
        }
        if (value == null) {
            // big number must have Number value
            if (ntype == MonaNType.BD || ntype == MonaNType.BI) {
                throw new TypeBadCastException("Cannot get number value for BigNumber");
            }
            if (ntype.isIntegral()) {
                value = ntype.newIntegralNumber(integralValue);
            }
            else {
                value = ntype.newRealNumber(realValue);
            }
        }
        return value;
    }

    @Override
    public int intValue() {
        if (MonaNType.E.equals(ntype))
            throw new TypeBadCastException("Not a valid number type");
        return ntype.isIntegral()? (int)integralValue : (int)realValue;
    }

    @Override
    public long longValue() {
        if (MonaNType.E.equals(ntype))
            throw new TypeBadCastException("Not a valid number type");
        // TODO: 0.5 -> 0 is ok?
        return ntype.isIntegral()? integralValue : (long)realValue;
    }

    @Override
    public float floatValue() {
        if (MonaNType.E.equals(ntype))
            throw new TypeBadCastException("Not a valid number type");
        return ntype.isIntegral()? (float)integralValue : (float)realValue;
    }

    @Override
    public double doubleValue() {
        if (MonaNType.E.equals(ntype))
            throw new TypeBadCastException("Not a valid number type");
//        if (doubleValue == null) {
//            doubleValue = numberValue().doubleValue();
//        }
        return ntype.isIntegral()? (double)integralValue : realValue;
    }

    @Override
    public BigInteger bigIntValue() {
        if (MonaNType.E.equals(ntype))
            throw new TypeBadCastException("Not a valid number type");
        if (ntype == MonaNType.BI) return (BigInteger) numberValue();
        if (ntype == MonaNType.BD) return ((BigDecimal) numberValue()).toBigInteger();
        if (ntype.isIntegral()) return BigInteger.valueOf(integralValue);
        return BigDecimal.valueOf(doubleValue()).toBigInteger();
    }

    @Override
    public BigDecimal bigDecValue() {
        if (MonaNType.E.equals(ntype))
            throw new TypeBadCastException("Not a valid number type");
        if (ntype == MonaNType.BI) return new BigDecimal((BigInteger) numberValue());
        if (ntype == MonaNType.BD) return (BigDecimal) numberValue();
        if (ntype.isIntegral()) return new BigDecimal(integralValue);
        return BigDecimal.valueOf(realValue);
    }

    @Override
    public String stringValue() {
        if (MonaNType.E.equals(ntype)) return "";
        if (!ntype.isIntegral() && Double.isNaN(realValue)) return "";
        return numberValue().toString();
    }

    @Override
    public String toString() {
        return stringValue();
    }
}
