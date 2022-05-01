package org.lds56.mona.core.codegen;


import java.util.List;

/**
 * @author lds56
 * @date 2022/04/16
 */
public interface AbastractCodeGen<T> {

    T onEmpty();

    T onNull();

    T onInteger(int i);

    T onLong(long l);

    T onFloat(float f);

    T onDouble(double d);

    T onBoolean(boolean b);

    T onString(String str);

    T onList(List<T> l);

    T onSet(List<T> l);

    T onMap(String[] keys, List<T> values);

    T onRange(int start, int end);

    T onIdentity(String id);

    T onAdd(T lhs, T rhs);

    T onSub(T lhs, T rhs);

    T onMul(T lhs, T rhs);

    T onDiv(T lhs, T rhs);

    T onMod(T lhs, T rhs);

    T onShr(T lhs, T rhs);

    T onShl(T lhs, T rhs);

    T onEq(T lhs, T rhs);

    T onNeq(T lhs, T rhs);

    T onLt(T lhs, T rhs);

    T onLte(T lhs, T rhs);

    T onGt(T lhs, T rhs);

    T onGte(T lhs, T rhs);

    T onAnd(T lhs, T rhs);

    T onOr(T lhs, T rhs);

    T onBitAnd(T lhs, T rhs);

    T onBitOr(T lhs, T rhs);

    T onBitXor(T lhs, T rhs);

    T onNeg(T value);

    T onNot(T value);

    T onBitNot(T value);

    T onTernary(T cond, T lvalue, T rvalue);

    T onAssignment(String name, T value);

    T onFunction(List<String> argNames, T funcBody);

    T onArguments(List<T> argValues);

    T onFuncCall(T func, T args);

    T onComma(T last, T value);

    T onIf(T cond, T tvalue);

    T onIfElse(T cond, T tvalue, T fvalue);

    T onReturn(T value);

    T onContinue();

    T onBreak();

    T onWhile(T cond, T loopBody);

    T onForIn(String iterName, T list, T loopBody);

    // T onLambda(List<String> argNames, dx)


}
