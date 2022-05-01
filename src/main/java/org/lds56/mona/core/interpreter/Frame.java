package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;
import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @author lds56
 * @date 2022/04/21
 * @description Calling Frame
 *
 */
public class Frame {

    private MonaObject[] locals;

    private OperandStack<MonaObject> operandStack;

    public Frame() {
        this.operandStack = new OperandStack<>();
    }

    public Frame(MonaObject[] locals) {
        this.locals = locals;
        this.operandStack = new OperandStack<>();
    }

    public static Frame createWithLocals(MonaObject[] locals) {
        return new Frame(locals);
    }

    public static Frame createWithArgs(MonaObject[] args, int localNum) {
        if (localNum < args.length) {
            throw new InterpretErrorException("Too many args when creating frame");
        }
        MonaObject[] locals = new MonaObject[localNum];
        System.arraycopy(args, 0, locals, 0, args.length);
        return new Frame(locals);
    }

    public MonaObject popOperand() {
        return operandStack.pop();
    }

    public MonaObject topOperand() {
        return operandStack.top();
    }

    public void topOperand(MonaObject obj) {
        operandStack.top(obj);
    }

    public void pushOperand(MonaObject obj) {
        operandStack.push(obj);
    }

    public MonaObject getLocal(int idx) {
        if (idx < 0 || idx >= locals.length) {
            throw new InterpretErrorException("Get local out of bound");
        }
        return locals[idx];
    }

    public void setLocal(MonaObject obj, int idx) {
        locals[idx] = obj;
    }

    public MonaObject[] getLocals() {
        return locals;
    }

    public Object[] getOperands() {
        return operandStack.toArray();
    }

}
