package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaUndefined;

/**
 * @author lds56
 * @date 2022/04/21
 * @description Calling Frame
 *
 */
public class Frame {

    private String[] localNames;

    private MonaObject[] locals;

    private final OperandStack<MonaObject> operandStack;

    private Frame outerFrame;

    private Frame freeFrame;

    public Frame() {
        this.operandStack = new OperandStack<>();
    }

    public Frame(String[] localNames, MonaObject[] locals) {
        this.localNames = localNames;
        this.locals = locals;
        this.operandStack = new OperandStack<>();
        this.outerFrame = null;
        this.freeFrame = null;
    }

    public static Frame createWithLocals(String[] localNames, MonaObject[] locals) {
        return new Frame(localNames, locals);
    }

    public static Frame createWithArgs(String[] localNames, MonaObject[] args) {
        int localNum = localNames.length;
        if (localNum < args.length) {
            throw new InterpretErrorException("Too many args when creating frame");
        }
        MonaObject[] locals = new MonaObject[localNum];
        for (int i=0; i<localNum; i++) {
            locals[i] = i < args.length ? args[i] : MonaUndefined.UNDEF;
        }
        System.arraycopy(args, 0, locals, 0, args.length);
        return new Frame(localNames, locals);
    }

    public Frame withOuter(Frame outerFrame) {
        this.outerFrame = outerFrame;
        return this;
    }

    public Frame withFree(Frame freeFrame) {
        this.freeFrame = freeFrame;
        return this;
    }

    public Frame getOuter() {
        return outerFrame;
    }

    public Frame getFree() {
        return freeFrame;
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

    public void setLocal(int idx, MonaObject obj) {
        locals[idx] = obj;
    }

    public String getLocalName(int idx) {
        return localNames[idx];
    }

    public MonaObject[] getLocals() {
        return locals;
    }

    public int getLocalNum() {
        return locals.length;
    }

    public Object[] getOperands() {
        return operandStack.toArray();
    }

}
