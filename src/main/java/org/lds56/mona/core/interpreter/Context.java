package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaUndefined;

import java.util.Map;
import java.util.Objects;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Context when carrying out instruction
 *
 */
public class Context {

    private StaticArea _static;

    private Frame _frame;

    private BasicBlock _block;

    public int _pc;        // program counter

    public Context() {
        this(0, null, null, null);
    }

    public Context(StaticArea staticArea) {
        this(0, null, null, staticArea);
    }

    public Context(int pc, Frame frame, BasicBlock block, StaticArea staticArea) {
        this._pc = pc;
        this._frame = frame;
        this._block = block;
        this._static = staticArea;
    }

    public void update(int pc, Frame frame, BasicBlock block) {
        this._pc = pc;
        this._frame = frame;
        this._block = block;
    }

    public int pc() { return _pc; }

    public Frame frame() {
        return _frame;
    }

    public BasicBlock block() {
        return _block;
    }

    public void fillInputs(Map<String, Object> inputs) {
        for (int i=0; i<_static.size(); i++) {
            if (_static.getName(i).startsWith("$")) {
                String inputName = _static.getName(i).substring(1);
                if (inputs.containsKey(inputName)){
                    _static.set(i, MonaObject.wrap(inputs.get(inputName)));
                }
            }
        }
    }

    public MonaObject findGlobal(int globalIndex) {
        String varName = _block.getGlobalVarName(globalIndex);
        Integer varPos = _block.getGlobalPos(globalIndex);
        // firstly find in free frame
        if (_frame.getFree() != null) {
            if (Objects.equals(_frame.getFree().getLocalName(varPos), varName)) {
                return _frame.getFree().getLocal(varPos);
            }
        }
        // then find in outer frame
        Frame outer;
        while ((outer = _frame.getOuter()) != null) {
            if (Objects.equals(outer.getLocalName(varPos), varName)) {
                return outer.getLocal(varPos);
            }
        }
        return MonaUndefined.UNDEF;
    }

    public void setGlobal(int globalIndex, MonaObject value) {
        String varName = _block.getGlobalVarName(globalIndex);
        Integer varPos = _block.getGlobalPos(globalIndex);
        // firstly set in free frame
        if (_frame.getFree() != null) {
            if (Objects.equals(_frame.getFree().getLocalName(varPos), varName)) {
                _frame.getFree().setLocal(varPos, value);
                return;
            }
        }
        // then set in outer frame
        Frame outer;;
        while ((outer = _frame.getOuter()) != null) {
            if (Objects.equals(outer.getLocalName(varPos), varName)) {
                outer.setLocal(varPos, value);
                return;
            }
        }
    }

    public MonaObject findStatic(int idx) {
        return _static.get(idx);
    }

    public void setStatic(int idx, MonaObject value) {
        _static.set(idx, value);
    }

    public MonaObject[] popArgs(int argNum) {
        // rightmost is top
        // push args from left to right
        // pop args from right to left
        MonaObject[] args = new MonaObject[argNum];
        for (int i=0; i<argNum; i++) {
            args[argNum - i - 1] = _frame.popOperand();
        }
        return args;
    }

    public Object[] popArgsUnpacked(int argNum) {
        // rightmost is top
        // push args from left to right
        // pop args from right to left
        Object[] args = new Object[argNum];
        for (int i=0; i<argNum; i++) {
            args[argNum - i - 1] = _frame.popOperand().getValue();
        }
        return args;
    }

    // Operand Operation
    public void pushOperand(MonaObject obj) {
        this._frame.pushOperand(obj);
    }

    public MonaObject popOperand() {
        return this._frame.popOperand();
    }

    public MonaObject topOperand() {
        return this._frame.topOperand();
    }

    public void topOperand(MonaObject obj) {
        this._frame.topOperand(obj);
    }
}
