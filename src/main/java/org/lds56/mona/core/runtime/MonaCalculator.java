package org.lds56.mona.core.runtime;

// import org.lds56.mona.core.compiler.method.MethodFactory;
import org.lds56.mona.core.exception.CalcuationErrorException;
import org.lds56.mona.core.exception.MathCalcuationException;
import org.lds56.mona.core.exception.OperationNotSupportedException;
import org.lds56.mona.core.exception.TypeBadCastException;
import org.lds56.mona.core.runtime.types.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.util.Objects;

public class MonaCalculator {

    /* Artihmetic */
    // binary ops
    public static MonaObject add(MonaObject a, MonaObject b) {

        // string concat
        if (MonaType.isString(a) || MonaType.isString(b)) {
            return MonaString.valueOf(a.stringValue() + b.stringValue());
        }

        // arithmetic add
        MonaNType resNType = MonaNType.higher(a.getNType(), b.getNType());
        if (!MonaNType.E.equals(resNType)) {

            if (MonaNType.BD.equals(resNType)) {
                return MonaNumber.valueOf(a.bigDecValue().add(b.bigDecValue()), MonaNType.BD);
            }
            if (MonaNType.BI.equals(resNType)) {
                return MonaNumber.valueOf(a.bigIntValue().add(b.bigIntValue()), MonaNType.BI);
            }

            if (resNType.isIntegral()) return MonaNumber.valueOf(a.longValue() + b.longValue(), resNType);
            else return MonaNumber.valueOf(a.doubleValue() + b.doubleValue(), resNType);
        }

//        if (a.getValue() instanceof Computable && b.getValue() instanceof Computable) {
//            return MonaObject.wrap(
//                    MethodFactory.invokeByName(a.getValue(), OperatorEnum.ADD.getMethodName(), b.getValue()));
//        }

        throw new OperationNotSupportedException("`Add` operation not suppoerted!");
    }

    public static MonaObject sub(MonaObject a, MonaObject b) {

        // arithmetic sub
        MonaNType resNType = MonaNType.higher(a.getNType(), b.getNType());
        if (!MonaNType.E.equals(resNType)) {

            if (MonaType.isBigDecimal(a) || MonaType.isBigDecimal(b)) {
                return MonaNumber.valueOf(a.bigDecValue().subtract(b.bigDecValue()), MonaNType.BD);
            }
            if (MonaType.isBigInteger(a) || MonaType.isBigInteger(b)) {
                return MonaNumber.valueOf(a.bigIntValue().subtract(b.bigIntValue()), MonaNType.BI);
            }

            if (resNType.isIntegral()) return MonaNumber.valueOf(a.longValue() - b.longValue(), resNType);
            else return MonaNumber.valueOf(a.doubleValue() - b.doubleValue(), resNType);
        }

//        if (a.getValue() instanceof Computable && b.getValue() instanceof Computable) {
//            return MonaObject.wrap(MethodFactory.invokeByName(a.getValue(), OperatorEnum.SUB.getMethodName(), b.getValue()));
//        }

        throw new OperationNotSupportedException("`Sub` operation not suppoerted!");
    }

    public static MonaObject mult(MonaObject a, MonaObject b) {

        // arithmetic mult
        MonaNType resNType = MonaNType.higher(a.getNType(), b.getNType());
        if (!MonaNType.E.equals(resNType)) {

            if (MonaType.isBigDecimal(a) || MonaType.isBigDecimal(b)) {
                return MonaNumber.valueOf(a.bigDecValue().multiply(b.bigDecValue()), MonaNType.BD);
            }
            if (MonaType.isBigInteger(a) || MonaType.isBigInteger(b)) {
                return MonaNumber.valueOf(a.bigIntValue().multiply(b.bigIntValue()), MonaNType.BI);
            }

            if (resNType.isIntegral()) return MonaNumber.valueOf(a.longValue() * b.longValue(), resNType);
            else return MonaNumber.valueOf(a.doubleValue() * b.doubleValue(), resNType);
        }

//        if (a.getValue() instanceof Computable && b.getValue() instanceof Computable) {
//            return MonaObject.wrap(MethodFactory.invokeByName(a.getValue(), OperatorEnum.MUL.getMethodName(), b.getValue()));
//        }

        throw new OperationNotSupportedException("`Sub` operation not suppoerted!");
    }

    public static MonaObject div(MonaObject a, MonaObject b) {

        // arithmetic div
        MonaNType resNType = MonaNType.higher(a.getNType(), b.getNType());
        if (!MonaNType.E.equals(resNType)) {

            // bigdecimal divide
            if (MonaType.isBigDecimal(a) || MonaType.isBigDecimal(b)) {
                BigDecimal bd = b.bigDecValue();
                if (bd.equals(BigDecimal.ZERO)) {
                    throw new MathCalcuationException("Divided by zero");
                }
                return MonaNumber.valueOf(a.bigDecValue().divide(bd, MathContext.DECIMAL128), MonaNType.BD);
            }
            // biginteger divide
            if (MonaType.isBigInteger(a) || MonaType.isBigInteger(b)) {
                BigInteger bi = b.bigIntValue();
                if (bi.equals(BigInteger.ZERO)) {
                    throw new MathCalcuationException("Divided by zero");
                }
                return MonaNumber.valueOf(a.bigIntValue().divide(bi), MonaNType.BI);
            }
            // small number dividee
            if (MonaType.isNumber(b) && b.doubleValue() == 0) {
                throw new MathCalcuationException("Divided by zero");
            }

            if (resNType.isIntegral()) return MonaNumber.valueOf(a.longValue() / b.longValue(), resNType);
            else return MonaNumber.valueOf(a.doubleValue() / b.doubleValue(), resNType);
        }

//        if (a.getValue() instanceof Computable && b.getValue() instanceof Computable) {
//            return MonaObject.wrap(MethodFactory.invokeByName(a.getValue(), OperatorEnum.DIV.getMethodName(), b.getValue()));
//        }

        throw new OperationNotSupportedException("`Div` operation not suppoerted!");
    }

    public static MonaObject mod(MonaObject a, MonaObject b) {

        MonaNType resNType = MonaNType.higher(a.getNType(), b.getNType());
        if (!MonaNType.E.equals(resNType)) {

            if (MonaType.isBigDecimal(a) || MonaType.isBigDecimal(b)) {
                return MonaNumber.valueOf(a.bigDecValue().remainder(b.bigDecValue(), MathContext.DECIMAL128),
                                             MonaNType.BD);
            }
            if (MonaType.isBigInteger(a) || MonaType.isBigInteger(b)) {
                return MonaNumber.valueOf(a.bigIntValue().mod(b.bigIntValue()), MonaNType.BI);
            }

            if (resNType.isIntegral()) return MonaNumber.valueOf(a.longValue() % b.longValue(), resNType);
            else return MonaNumber.valueOf(a.doubleValue() % b.doubleValue(), resNType);
        }

//        if (a.getValue() instanceof Computable && b.getValue() instanceof Computable) {
//            return MonaObject.wrap(MethodFactory.invokeByName(a.getValue(), OperatorEnum.MOD.getMethodName(), b.getValue()));
//        }

        throw new OperationNotSupportedException("`Mod` operation not suppoerted!");
    }

    public static MonaObject neg(MonaObject a) {

        MonaNType resNType = a.getNType();
        if (!MonaNType.E.equals(resNType)) {

            if (MonaNType.BD.equals(resNType)) {
                return MonaNumber.valueOf(a.bigDecValue().negate(), MonaNType.BD);
            }
            if (MonaNType.BI.equals(resNType)) {
                return MonaNumber.valueOf(a.bigIntValue().negate(), MonaNType.BI);
            }

            if (resNType.isIntegral()) return MonaNumber.valueOf(-a.longValue(), resNType);
            else return MonaNumber.valueOf(-a.doubleValue(), resNType);
        }

//        if (a.getValue() instanceof Computable) {
//            return MonaObject.wrap(MethodFactory.invokeByName(a.getValue(), OperatorEnum.NEG.getMethodName()));
//        }

        throw new OperationNotSupportedException("`Neg` operation not suppoerted!");
    }

    /* Logic */
    public static int cmp(MonaObject a, MonaObject b) {
        if (MonaType.isNull(a) || MonaType.isNull(b)) {
            throw new CalcuationErrorException("Cannot compare with null");
        }
        if (MonaType.isBigDecimal(a) || MonaType.isBigDecimal(b)) {
            return a.bigDecValue().compareTo(b.bigDecValue());
        }
        if (MonaType.isBigInteger(a) || MonaType.isBigInteger(b)) {
            return a.bigIntValue().compareTo(b.bigIntValue());
        }
        if (MonaType.isNumber(a) || MonaType.isNumber(b)) {
            return Double.compare(a.doubleValue(), b.doubleValue());
        }
        if (MonaType.isString(a) || MonaType.isString(b)) {
            return a.stringValue().compareTo(b.stringValue());
        }
        Object aValue = a.getValue();
        Object bValue = b.getValue();
        if (aValue instanceof Comparable<?>) {
            @SuppressWarnings("unchecked") // OK because of instanceof check above
            final Comparable<Object> comparable = (Comparable<Object>) aValue;
            return comparable.compareTo(bValue);
        }
        if (bValue instanceof Comparable<?>) {
            @SuppressWarnings("unchecked") // OK because of instanceof check above
            final Comparable<Object> comparable = (Comparable<Object>) bValue;
            return comparable.compareTo(aValue);
        }
        return 1;
    }

    public static MonaObject eq(MonaObject a, MonaObject b) {
        if (Objects.equals(a, b)) return MonaBoolean.TRUE;
        if (MonaType.isNull(a) || MonaType.isNull(b)) return MonaBoolean.FALSE;
        try {
            if (MonaType.isBoolean(a) || MonaType.isBoolean(b)) {
                return MonaBoolean.valueOf(a.booleanValue() == b.booleanValue());
            }
            if (MonaType.isBigDecimal(a) || MonaType.isBigDecimal(b)) {
                return MonaBoolean.valueOf(a.bigDecValue().equals(b.bigDecValue()));
            }
            if (MonaType.isBigInteger(a) || MonaType.isBigInteger(b)) {
                return MonaBoolean.valueOf(a.bigIntValue().equals(b.bigIntValue()));
            }
            if (MonaType.isNumber(a) || MonaType.isNumber(b)) {
                return MonaBoolean.valueOf(a.doubleValue() == b.doubleValue());
            }
        } catch (TypeBadCastException e) {
            return MonaBoolean.FALSE;
        }
        return MonaBoolean.valueOf(Objects.equals(a.getValue(), b.getValue()));
    }

    public static MonaObject neq(MonaObject a, MonaObject b) {
        return not(eq(a, b));
    }

    public static MonaObject gt(MonaObject a, MonaObject b) {
        if (MonaType.isNull(a) || MonaType.isNull(b)) return MonaBoolean.FALSE;
        return MonaBoolean.valueOf(cmp(a, b) > 0);
    }

    public static MonaObject lt(MonaObject a, MonaObject b) {
        if (MonaType.isNull(a) || MonaType.isNull(b)) return MonaBoolean.FALSE;
        return MonaBoolean.valueOf(cmp(a, b) < 0);
    }

    public static MonaObject gte(MonaObject a, MonaObject b) {
        if (MonaType.isNull(a) || MonaType.isNull(b)) return MonaBoolean.FALSE;
        return MonaBoolean.valueOf(cmp(a, b) >= 0);
    }

    public static MonaObject lte(MonaObject a, MonaObject b) {
        if (MonaType.isNull(a) || MonaType.isNull(b)) return MonaBoolean.FALSE;
        return MonaBoolean.valueOf(cmp(a, b) <= 0);
    }

    public static MonaObject and(MonaObject a, MonaObject b) {
        return MonaBoolean.valueOf(a.booleanValue() && b.booleanValue());
    }

    public static MonaObject or(MonaObject a, MonaObject b) {
        return MonaBoolean.valueOf(a.booleanValue() || b.booleanValue());
    }

    public static MonaObject not(MonaObject a) {
        return MonaBoolean.valueOf(!a.booleanValue());
    }

    /* Bit */
    public static MonaObject bitAnd(MonaObject a, MonaObject b) {
        return MonaNumber.valueOf(a.longValue() & b.longValue(), MonaNType.higher(a.getNType(), b.getNType()));
    }

    public static MonaObject bitOr(MonaObject a, MonaObject b) {
        return MonaNumber.valueOf(a.longValue() | b.longValue(), MonaNType.higher(a.getNType(), b.getNType()));
    }

    public static MonaObject bitXor(MonaObject a, MonaObject b) {
        return MonaNumber.valueOf(a.longValue() ^ b.longValue(), MonaNType.higher(a.getNType(), b.getNType()));
    }

    public static MonaObject bitNot(MonaObject a) {
        return MonaNumber.valueOf(~a.longValue(), a.getNType());
    }
}
