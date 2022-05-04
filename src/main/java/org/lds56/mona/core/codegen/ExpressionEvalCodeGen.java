package org.lds56.mona.core.codegen;

import org.lds56.mona.core.exception.MonaRuntimeException;
import org.lds56.mona.core.exception.SyntaxNotSupportedException;
import org.lds56.mona.core.runtime.MonaCalculator;
import org.lds56.mona.core.runtime.types.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lds56
 * @date 2022/04/17
 */
public class ExpressionEvalCodeGen implements AbastractCodeGen<MonaObject> {

    private final Map<String, MonaObject> env = new HashMap<>();

    public void withContext(Map<String, Object> ctx) {
        for (Map.Entry<String, Object> entry : ctx.entrySet()) {
            this.env.put(entry.getKey(), MonaObject.wrap(entry.getValue()));
        }
    }

    @Override
    public MonaObject onEmpty() {
        return MonaNull.NIL;
    }

    @Override
    public MonaObject onNull() {
        return MonaNull.NIL;
    }

    @Override
    public MonaObject onString(String str) {
        return MonaString.valueOf(str);
    }

    @Override
    public MonaObject onList(List<MonaObject> l) {
        return null;
    }

    @Override
    public MonaObject onSet(List<MonaObject> l) {
        return null;
    }

    @Override
    public MonaObject onMap(String[] keys, List<MonaObject> values) {
        return null;
    }

    @Override
    public MonaObject onRange(int start, int end) {
        return null;
    }

    @Override
    public MonaObject onInteger(int i) {
        return MonaNumber.newInteger(i);
    }

    @Override
    public MonaObject onLong(long l) {
        return MonaNumber.newLong(l);
    }

    @Override
    public MonaObject onFloat(float f) {
        return MonaNumber.newFloat(f);
    }

    @Override
    public MonaObject onDouble(double d) {
        return MonaNumber.newDouble(d);
    }

    @Override
    public MonaObject onBoolean(boolean b) {
        return MonaBoolean.valueOf(b);
    }

    @Override
    public MonaObject onIdentity(String id) {
        return env.get(id);
    }

    @Override
    public MonaObject onAdd(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.add(lhs, rhs);
    }

    @Override
    public MonaObject onSub(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.sub(lhs, rhs);
    }

    @Override
    public MonaObject onMul(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.mult(lhs, rhs);
    }

    @Override
    public MonaObject onDiv(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.div(lhs, rhs);
    }

    @Override
    public MonaObject onMod(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.mod(lhs, rhs);
    }

    @Override
    public MonaObject onShr(MonaObject lhs, MonaObject rhs) {
        return null;
    }

    @Override
    public MonaObject onShl(MonaObject lhs, MonaObject rhs) {
        return null;
    }

    @Override
    public MonaObject onEq(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.eq(lhs, rhs);
    }

    @Override
    public MonaObject onNeq(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.neq(lhs, rhs);
    }

    @Override
    public MonaObject onLt(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.lt(lhs, rhs);
    }

    @Override
    public MonaObject onLte(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.lte(lhs, rhs);
    }

    @Override
    public MonaObject onGt(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.gt(lhs, rhs);
    }

    @Override
    public MonaObject onGte(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.gte(lhs, rhs);
    }

    @Override
    public MonaObject onAnd(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.and(lhs, rhs);
    }

    @Override
    public MonaObject onOr(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.or(lhs, rhs);
    }

    @Override
    public MonaObject onBitAnd(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.bitAnd(lhs, rhs);
    }

    @Override
    public MonaObject onBitOr(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.bitOr(lhs, rhs);
    }

    @Override
    public MonaObject onBitXor(MonaObject lhs, MonaObject rhs) {
        return MonaCalculator.bitXor(lhs, rhs);
    }

    @Override
    public MonaObject onNeg(MonaObject value) {
        return MonaCalculator.neg(value);
    }

    @Override
    public MonaObject onBitNot(MonaObject value) {
        return MonaCalculator.bitNot(value);
    }

    @Override
    public MonaObject onNot(MonaObject value) {
        return MonaCalculator.not(value);
    }

    @Override
    public MonaObject onTernary(MonaObject cond, MonaObject lvalue, MonaObject rvalue) {
        return cond.booleanValue() ? lvalue : rvalue;
    }

    @Override
    public MonaObject onAssignment(String name, MonaObject value) {
        return env.put(name, value);
    }

    @Override
    public MonaObject onFunction(List<String> argNames, MonaObject funcBody) {
        throw new SyntaxNotSupportedException("`Func Def` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onArguments(List<MonaObject> argValues) {
        return MonaTuple.fromList(argValues);
    }

    @Override
    public MonaObject onFuncCall(MonaObject func, MonaObject args) {
        if (args instanceof MonaTuple) {
            return func.invoke(((MonaTuple)args).toArray());
        }
        throw new MonaRuntimeException("Invalid arguments for func call");
    }

    @Override
    public MonaObject onComma(MonaObject last, MonaObject value) {
        return value;
    }

    @Override
    public MonaObject onIf(MonaObject cond, MonaObject tvalue) {
        throw new SyntaxNotSupportedException("`If` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onIfElse(MonaObject cond, MonaObject tvalue, MonaObject fvalue) {
        throw new SyntaxNotSupportedException("`If Else` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onReturn(MonaObject value) {
        throw new SyntaxNotSupportedException("`Return` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onContinue() {
        throw new SyntaxNotSupportedException("`Continue` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onBreak() {
        throw new SyntaxNotSupportedException("`Break` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onWhile(MonaObject cond, MonaObject loopBody) {
        throw new SyntaxNotSupportedException("`While` is not supported in expression evaluation");
    }

    @Override
    public MonaObject onForIn(String iterName, MonaObject list, MonaObject loopBody) {
        throw new SyntaxNotSupportedException("`For-in` is not supported in expression evaluation");
    }

}
