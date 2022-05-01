package org.lds56.mona.core.interpreter.ir;

import org.lds56.mona.core.interpreter.Context;
import org.lds56.mona.core.runtime.MonaCalculator;
import org.lds56.mona.core.runtime.types.MonaBB;
import org.lds56.mona.core.runtime.types.MonaBoolean;
import org.lds56.mona.core.runtime.types.MonaIter;
import org.lds56.mona.core.runtime.types.MonaObject;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

public class OpLogic {

    // Stack
    public static Signal PopTop(Context context, Integer unused) {
        context.frame().popOperand();
        return Signal.emitNext();
    }

    // Arithmetic
    public static Signal Add(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::add);
    }

    public static Signal Substract(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::sub);
    }

    public static Signal Multiply(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::mult);
    }

    public static Signal Divide(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::div);
    }

    public static Signal Modulo(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::mod);
    }

    public static Signal Postive(Context context, Integer unused) {
        return Signal.emitNext();
    }

    public static Signal Negative(Context context, Integer unused) {
        return UnaryOpLogic(context, MonaCalculator::neg);
    }

    // Logic
    public static Signal LogicAnd(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::and);
    }

    public static Signal LogicOr(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::or);
    }

    public static Signal LogicNot(Context context, Integer unused) {
        return UnaryOpLogic(context, MonaCalculator::not);
    }

    // Bitwise
    public static Signal BitAnd(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::bitAnd);
    }

    public static Signal BitOr(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::bitOr);
    }

    public static Signal BitXor(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::bitXor);
    }

    public static Signal BitNot(Context context, Integer unused) {
        return UnaryOpLogic(context, MonaCalculator::bitNot);
    }

    // Inplace Arithmetic
    // TODO: support inplace add
    public static Signal InplaceAdd(Context context, Integer unused) {
        MonaObject rhs = context.frame().popOperand();
        MonaObject lhs = context.frame().popOperand();
        context.frame().pushOperand(MonaCalculator.add(lhs, rhs));
        return Signal.emitNext();
    }

    // Comparation
    public static Signal Equal(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::eq);
    }

    public static Signal NotEqual(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::neq);
    }

    public static Signal GreaterThan(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::gt);
    }

    public static Signal LessThan(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::lt);
    }

    public static Signal GreaterThanOrEqual(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::gte);
    }

    public static Signal LessThanOrEqual(Context context, Integer unused) {
        return BinaryOpLogic(context, MonaCalculator::lte);
    }

    // Data
    public static Signal LoadLocal(Context context, Integer index) {
        MonaObject value = context.frame().getLocal(index);
        context.frame().pushOperand(value);
        return Signal.emitNext();
    }

    public static Signal LoadConst(Context context, Integer index) {
        MonaObject value = context.block().loadConst(index);
        context.frame().pushOperand(value);
        return Signal.emitNext();
    }

    public static Signal StoreLocal(Context context, Integer index) {
        MonaObject value = context.frame().popOperand();
        context.frame().setLocal(value, index);
        return Signal.emitNext();
    }


    public static Signal GetIterator(Context context, Integer unused) {
        MonaObject iter = context.frame().popOperand().iter();
        context.frame().pushOperand(iter);
        return Signal.emitNext();
    }

    // if has next, push next value & true, else only pop self and push false
    public static Signal NextIterator(Context context, Integer unused) {
        // no pop
        MonaIter iter = (MonaIter) context.frame().topOperand();
        if (iter.hasNext()) {
            context.frame().pushOperand(iter.next());
            context.frame().pushOperand(MonaBoolean.TRUE);
        }
        else {
            context.frame().topOperand(MonaBoolean.FALSE);
        }
        return Signal.emitNext();
    }

    public static Signal JumpLocal(Context context, Integer index) {
        return Signal.emitJump(context.block().startAddress + index);
    }

    public static Signal JumpGlobal(Context context, Integer index) {
        return Signal.emitJump(index);
    }

    public static Signal JumpOffset(Context context, Integer index) {
        return Signal.emitJump(context.pc() + index);
    }

    public static Signal BranchTrue(Context context, Integer index) {
        if (context.frame().popOperand().booleanValue()) {
            return Signal.emitJump(context.block().startAddress + index);
        } else {
            return Signal.emitNext();
        }
    }

    public static Signal BranchFalse(Context context, Integer index) {
        if (!context.frame().popOperand().booleanValue()) {
            return Signal.emitJump(context.block().startAddress + index);
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
        return Signal.emitRet(context.frame().popOperand());
    }

    public static Signal CallFunction(Context context, Integer argNum) {
        // rightmost is top
        // push args from left to right
        // pop args from right to left
        MonaObject[] args = new MonaObject[argNum];
        for (int i=0; i<argNum; i++) {
            args[argNum - i - 1] = context.frame().popOperand();
        }
        return Signal.emitCall(context.frame().popOperand().intValue(), args);
    }

    public static Signal MakeFunction(Context context, Integer flag) {
        context.frame().pushOperand(new MonaBB(flag));
        return Signal.emitNext();
    }

    private static Signal UnaryOpLogic(Context context, UnaryOperator<MonaObject> unaryOp) {
        MonaObject value = context.frame().popOperand();
        context.frame().pushOperand(unaryOp.apply(value));
        return Signal.emitNext();
    }

    private static Signal BinaryOpLogic(Context context, BinaryOperator<MonaObject> binaryOp) {
        MonaObject rhs = context.frame().popOperand();
        MonaObject lhs = context.frame().popOperand();
        context.frame().pushOperand(binaryOp.apply(lhs, rhs));
        return Signal.emitNext();
    }


    private static Signal BranchBinaryOpLogic(Context context, Integer index, BinaryOperator<MonaObject> binaryOp) {
        MonaObject rhs = context.frame().popOperand();
        MonaObject lhs = context.frame().popOperand();
        if (binaryOp.apply(lhs, rhs).booleanValue()) {
            return Signal.emitJump(context.block().startAddress + index);
        } else {
            return Signal.emitNext();
        }
    }
}
