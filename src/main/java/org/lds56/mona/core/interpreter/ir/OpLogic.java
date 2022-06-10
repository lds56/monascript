package org.lds56.mona.core.interpreter.ir;

import org.lds56.mona.core.interpreter.Context;
import org.lds56.mona.core.interpreter.MonaBB;
import org.lds56.mona.core.runtime.MonaCalculator;
import org.lds56.mona.core.runtime.collections.MonaIter;
import org.lds56.mona.core.runtime.traits.*;
import org.lds56.mona.core.runtime.types.MonaBoolean;
import org.lds56.mona.core.runtime.types.MonaObject;

import java.util.function.BinaryOperator;

public class OpLogic {

    // Stack
    public static Signal PopTop(Context context, Integer unused) {
        context.popOperand();
        return Signal.emitNext();
    }

    // Arithmetic
    public static Signal Add(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.add(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal Substract(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.sub(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal Multiply(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.mult(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal Divide(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.div(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal Modulo(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.mod(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal Postive(Context context, Integer unused) {
        return Signal.emitNext();
    }

    public static Signal Negative(Context context, Integer unused) {
        context.pushOperand(MonaCalculator.neg(context.popOperand()));
        return Signal.emitNext();
    }

    // Logic
    public static Signal LogicAnd(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.and(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal LogicOr(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.or(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal LogicNot(Context context, Integer unused) {
        context.pushOperand(MonaCalculator.not(context.popOperand()));
        return Signal.emitNext();
    }

    // Bitwise
    public static Signal BitAnd(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.bitAnd(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal BitOr(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.bitOr(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal BitXor(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.bitXor(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal BitNot(Context context, Integer unused) {
        context.pushOperand(MonaCalculator.bitNot(context.popOperand()));
        return Signal.emitNext();
    }

    // Inplace Arithmetic
    public static Signal InplaceAdd(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        context.topOperand(MonaCalculator.add(context.topOperand(), rhs));
        return Signal.emitNext();
    }

    public static Signal InplaceSub(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        context.topOperand(MonaCalculator.sub(context.topOperand(), rhs));
        return Signal.emitNext();
    }

    // Comparation
    public static Signal Equal(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.eq(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal NotEqual(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.neq(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal GreaterThan(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.gt(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal LessThan(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.lt(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal GreaterThanOrEqual(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.gte(lhs, rhs));
        return Signal.emitNext();
    }

    public static Signal LessThanOrEqual(Context context, Integer unused) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        context.pushOperand(MonaCalculator.lte(lhs, rhs));
        return Signal.emitNext();
    }

    // Data
    public static Signal LoadLocal(Context context, Integer index) {
        MonaObject value = context.frame().getLocal(index);
        context.pushOperand(value);
        return Signal.emitNext();
    }

    public static Signal LoadGlobal(Context context, Integer index) {
        MonaObject value = context.findGlobal(index);
        context.pushOperand(value);
        return Signal.emitNext();
    }

    public static Signal LoadConst(Context context, Integer index) {
        MonaObject value = context.block().loadConst(index);
        context.pushOperand(value);
        return Signal.emitNext();
    }

    public static Signal StoreLocal(Context context, Integer index) {
        MonaObject value = context.popOperand();
        context.frame().setLocal(index, value);
        return Signal.emitNext();
    }

    public static Signal StoreGlobal(Context context, Integer index) {
        MonaObject value = context.popOperand();
        context.setGlobal(-index, value);
        return Signal.emitNext();
    }

    public static Signal GetIterator(Context context, Integer unused) {
        MonaIterable coll = MonaTrait.cast(context.popOperand(), MonaIterable.class);
        context.pushOperand(coll.iter());
        return Signal.emitNext();
    }

    // if has next, push next value & true, else only pop self and push false
    public static Signal NextIterator(Context context, Integer unused) {
        // no pop
        MonaIter iter = (MonaIter) context.topOperand();
        if (iter.hasNext()) {
            context.pushOperand(iter.next());
            context.pushOperand(MonaBoolean.TRUE);
        }
        else {
            context.topOperand(MonaBoolean.FALSE);
        }
        return Signal.emitNext();
    }

    public static Signal IndexAccess(Context context, Integer unused) {
        MonaObject index = context.popOperand();
        MonaIndexable indexable = MonaTrait.cast(context.popOperand(), MonaIndexable.class);
        context.pushOperand(indexable.index(index));
        return Signal.emitNext();
    }

    public static Signal PropAccess(Context context, Integer unused) {
        MonaObject prop = context.popOperand();
        MonaAccessible obj = MonaTrait.cast(context.popOperand(), MonaAccessible.class);
        context.pushOperand(obj.getProperty(prop.stringValue()));
        return Signal.emitNext();
    }

    public static Signal JumpLocal(Context context, Integer index) {
        return Signal.emitJump(context.block().startAddress() + index);
    }

    public static Signal JumpGlobal(Context context, Integer index) {
        return Signal.emitJump(index);
    }

    public static Signal JumpOffset(Context context, Integer index) {
        return Signal.emitJump(context.pc() + index);
    }

    public static Signal BranchTrue(Context context, Integer index) {
        if (context.popOperand().booleanValue()) {
            return Signal.emitJump(context.block().startAddress() + index);
        } else {
            return Signal.emitNext();
        }
    }

    public static Signal BranchFalse(Context context, Integer index) {
        if (!context.popOperand().booleanValue()) {
            return Signal.emitJump(context.block().startAddress() + index);
        } else {
            return Signal.emitNext();
        }
    }

    public static Signal BranchEqual(Context context, Integer index) {
        return BranchBinaryOpLogic(context, index, MonaCalculator::eq);
    }

    public static Signal BranchNotEqual(Context context, Integer index) {
        return BranchBinaryOpLogic(context, index, MonaCalculator::neq);
    }

    public static Signal BranchGreaterThan(Context context, Integer index) {
        return BranchBinaryOpLogic(context, index, MonaCalculator::gt);
    }

    public static Signal BranchGreaterThanOrEqual(Context context, Integer index) {
        return BranchBinaryOpLogic(context, index, MonaCalculator::gte);
    }

    public static Signal BranchLessThan(Context context, Integer index) {
        return BranchBinaryOpLogic(context, index, MonaCalculator::lt);
    }

    public static Signal BranchLessThanOrEqual(Context context, Integer index) {
        return BranchBinaryOpLogic(context, index, MonaCalculator::lte);
    }

    public static Signal ReturnValue(Context context, Integer unused) {
        return Signal.emitRet(context.popOperand());
    }

    public static Signal CallFunction(Context context, Integer argNum) {
        MonaObject[] args = context.popArgs(argNum);
        MonaObject func = context.popOperand();
        return Signal.emitCall(func, args);
    }

    public static Signal CallMethod(Context context, Integer argNum) {
        Object[] args = context.popArgsUnpacked(argNum);
        MonaObject methodName = context.popOperand();
        MonaAccessible obj = MonaTrait.cast(context.popOperand(), MonaAccessible.class);
        context.pushOperand(obj.callMethod(methodName.stringValue(), args));
        return Signal.emitNext();
    }

    public static Signal CallObject(Context context, Integer argNum) {
        MonaObject[] args = context.popArgs(argNum);
        MonaInvocable func = MonaTrait.cast(context.popOperand(), MonaInvocable.class);
        context.pushOperand(func.invoke(args));
        return Signal.emitNext();
    }

    public static Signal MakeFunction(Context context, Integer flag) {
        context.pushOperand(new MonaBB(flag));      // TODO: change MonaBB
        return Signal.emitNext();
    }

//    private static Signal UnaryOpLogic(Context context, UnaryOperator<MonaObject> unaryOp) {
//        MonaObject value = context.popOperand();
//        context.pushOperand(unaryOp.apply(value));
//        return Signal.emitNext();
//    }
//
//    private static Signal BinaryOpLogic(Context context, BinaryOperator<MonaObject> binaryOp) {
//        MonaObject rhs = context.popOperand();
//        MonaObject lhs = context.popOperand();
//        context.pushOperand(binaryOp.apply(lhs, rhs));
//        return Signal.emitNext();
//    }


    private static Signal BranchBinaryOpLogic(Context context, Integer index, BinaryOperator<MonaObject> binaryOp) {
        MonaObject rhs = context.popOperand();
        MonaObject lhs = context.popOperand();
        if (binaryOp.apply(lhs, rhs).booleanValue()) {
            return Signal.emitJump(context.block().startAddress() + index);
        } else {
            return Signal.emitNext();
        }
    }
}
