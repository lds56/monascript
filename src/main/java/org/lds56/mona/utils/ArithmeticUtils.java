package org.lds56.mona.utils;

import java.math.BigDecimal;
import java.math.BigInteger;

public class ArithmeticUtils {


    /** Double.MAX_VALUE as BigDecimal. */
    protected static final BigDecimal BIGD_DOUBLE_MAX_VALUE = BigDecimal.valueOf(Double.MAX_VALUE);

    /** Double.MIN_VALUE as BigDecimal. */
    protected static final BigDecimal BIGD_DOUBLE_MIN_VALUE = BigDecimal.valueOf(Double.MIN_VALUE);

    /** Long.MAX_VALUE as BigInteger. */
    protected static final BigInteger BIGI_LONG_MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE);

    /** Long.MIN_VALUE as BigInteger. */
    protected static final BigInteger BIGI_LONG_MIN_VALUE = BigInteger.valueOf(Long.MIN_VALUE);

    /**
     * Given a Number, return back the value using the smallest type the result
     * will fit into.
     * <p>This works hand in hand with parameter 'widening' in java
     * method calls, e.g. a call to substring(int,int) with an int and a long
     * will fail, but a call to substring(int,int) with an int and a short will
     * succeed.</p>
     *
     * @param original the original number.
     * @return a value of the smallest type the original number will fit into.
     */
    public static Number narrow(final Number original) {
        return narrowNumber(original, null);
    }

    /**
     * Whether we consider the narrow class as a potential candidate for narrowing the source.
     *
     * @param narrow the target narrow class
     * @param source the orginal source class
     * @return true if attempt to narrow source to target is accepted
     */
    protected static boolean narrowAccept(final Class<?> narrow, final Class<?> source) {
        return narrow == null || narrow.equals(source);
    }

    /**
     * Given a Number, return back the value attempting to narrow it to a target class.
     *
     * @param original the original number
     * @param narrow   the attempted target class
     * @return the narrowed number or the source if no narrowing was possible
     */
    public static Number narrowNumber(final Number original, final Class<?> narrow) {
        if (original == null) {
            return null;
        }
        Number result = original;
        if (original instanceof BigDecimal) {
            final BigDecimal bigd = (BigDecimal) original;
            // if it's bigger than a double it can't be narrowed
            if (bigd.compareTo(BIGD_DOUBLE_MAX_VALUE) > 0
                || bigd.compareTo(BIGD_DOUBLE_MIN_VALUE) < 0) {
                return original;
            }
            try {
                final long l = bigd.longValueExact();
                // coerce to int when possible (int being so often used in method parms)
                if (narrowAccept(narrow, Integer.class)
                    && l <= Integer.MAX_VALUE
                    && l >= Integer.MIN_VALUE) {
                    return (int) l;
                }
                if (narrowAccept(narrow, Long.class)) {
                    return l;
                }
            } catch (final ArithmeticException xa) {
                // ignore, no exact value possible
            }
        }
        if (original instanceof Double || original instanceof Float) {
            final double value = original.doubleValue();
            if (narrowAccept(narrow, Float.class)
                && value <= Float.MAX_VALUE
                && value >= Float.MIN_VALUE) {
                result = result.floatValue();
            }
            // else it fits in a double only
        } else {
            if (original instanceof BigInteger) {
                final BigInteger bigi = (BigInteger) original;
                // if it's bigger than a Long it can't be narrowed
                if (bigi.compareTo(BIGI_LONG_MAX_VALUE) > 0
                    || bigi.compareTo(BIGI_LONG_MIN_VALUE) < 0) {
                    return original;
                }
            }
            final long value = original.longValue();
            if (narrowAccept(narrow, Byte.class)
                && value <= Byte.MAX_VALUE
                && value >= Byte.MIN_VALUE) {
                // it will fit in a byte
                result = (byte) value;
            } else if (narrowAccept(narrow, Short.class)
                && value <= Short.MAX_VALUE
                && value >= Short.MIN_VALUE) {
                result = (short) value;
            } else if (narrowAccept(narrow, Integer.class)
                && value <= Integer.MAX_VALUE
                && value >= Integer.MIN_VALUE) {
                result = (int) value;
            }
            // else it fits in a long
        }
        return result;
    }

    /**
     * Given a BigInteger, narrow it to an Integer or Long if it fits and the arguments
     * class allow it.
     * <p>
     * The rules are:
     * if either arguments is a BigInteger, no narrowing will occur
     * if either arguments is a Long, no narrowing to Integer will occur
     * </p>
     *
     * @param lhs  the left hand side operand that lead to the bigi result
     * @param rhs  the right hand side operand that lead to the bigi result
     * @param bigi the BigInteger to narrow
     * @return an Integer or Long if narrowing is possible, the original BigInteger otherwise
     */
    protected static Number narrowBigInteger(final Object lhs, final Object rhs, final BigInteger bigi) {
        //coerce to long if possible
        if (!(lhs instanceof BigInteger || rhs instanceof BigInteger)
            && bigi.compareTo(BIGI_LONG_MAX_VALUE) <= 0
            && bigi.compareTo(BIGI_LONG_MIN_VALUE) >= 0) {
            // coerce to int if possible
            final long l = bigi.longValue();
            // coerce to int when possible (int being so often used in method parms)
            if (!(lhs instanceof Long || rhs instanceof Long)
                && l <= Integer.MAX_VALUE
                && l >= Integer.MIN_VALUE) {
                return (int) l;
            }
            return l;
        }
        return bigi;
    }

    /**
     * Given a BigDecimal, attempt to narrow it to an Integer or Long if it fits if
     * one of the arguments is a numberable.
     *
     * @param lhs  the left hand side operand that lead to the bigd result
     * @param rhs  the right hand side operand that lead to the bigd result
     * @param bigd the BigDecimal to narrow
     * @return an Integer or Long if narrowing is possible, the original BigInteger otherwise
     */
    protected static Number narrowBigDecimal(final Object lhs, final Object rhs, final BigDecimal bigd) {
        if (isNumberable(lhs) || isNumberable(rhs)) {
            try {
                final long l = bigd.longValueExact();
                // coerce to int when possible (int being so often used in method parms)
                if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
                    return (int) l;
                }
                return l;
            } catch (final ArithmeticException xa) {
                // ignore, no exact value possible
            }
        }
        return bigd;
    }

    /**
     * Replace all numbers in an arguments array with the smallest type that will fit.
     *
     * @param args the argument array
     * @return true if some arguments were narrowed and args array is modified,
     *         false if no narrowing occurred and args array has not been modified
     */
    public static boolean narrowArguments(final Object[] args) {
        boolean narrowed = false;
        if (args != null) {
            for (int a = 0; a < args.length; ++a) {
                final Object arg = args[a];
                if (arg instanceof Number) {
                    final Number narg = (Number) arg;
                    final Number narrow = narrow(narg);
                    if (!narg.equals(narrow)) {
                        args[a] = narrow;
                        narrowed = true;
                    }
                }
            }
        }
        return narrowed;
    }

    /**
     * Given a long, attempt to narrow it to an int.
     * <p>Narrowing will only occur if no operand is a Long.
     * @param lhs  the left hand side operand that lead to the long result
     * @param rhs  the right hand side operand that lead to the long result
     * @param r the long to narrow
     * @return an Integer if narrowing is possible, the original Long otherwise
     */
    protected static Number narrowLong(final Object lhs, final Object rhs, final long r) {
        if (!(lhs instanceof Long || rhs instanceof Long) && (int) r == r) {
            return (int) r;
        }
        return r;
    }

    /**
     * Is Object a whole number.
     *
     * @param o Object to be analyzed.
     * @return true if Integer, Long, Byte, Short or Character.
     */
    protected static boolean isNumberable(final Object o) {
        return o instanceof Integer
            || o instanceof Long
            || o instanceof Byte
            || o instanceof Short
            || o instanceof Character;
    }
}
