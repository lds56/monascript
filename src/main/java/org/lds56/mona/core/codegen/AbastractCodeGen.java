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

    T onMap(List<T> kv);

    T onRange(int start, int end);

    T onIdentity(String id);

    T onIndex(T obj, T index);

    T onProperty(T obj, String attr);

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
    
    T onSelfAdd(String name, T value);

    T onSelfSub(String name, T value);

    T onSelfMul(String name, T value);

    T onSelfDiv(String name, T value);

    T onSelfMod(String name, T value);

    T onDefinition(String name, T value);

    T onAssignment(String name, T value);

    T onDefinitionUnpacked(List<String> names, T value);

    T onParameters(List<String> argNames);

    T onFuncArgs(List<String> argNames);

    T onFunction(List<String> argNames, T funcBody);

    T onArguments(List<T> argValues);

    T onMemberCall(T obj, String memberName, List<T> args);

    T onFuncCall(T func, List<T> args);

    T onComma(T last, T value);

    T onIf(T cond, T tvalue);

    T onIfElse(T cond, T tvalue, T fvalue);

    T onReturn(List<T> value);

    T onNoneReturn();

    T onContinue();

    T onBreak();

    T onWhile(T cond, T loopBody);

    T onIter(String iterName);

    T onIterUnpacked(List<String> iterNames);

    T onForIn(T iter, T list, T loopBody);

    // T onLambda(List<String> argNames, dx)

}
