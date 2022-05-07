package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaUndefined;

import java.util.Objects;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Context when carrying out instruction
 *
 */
public class Context {

    private final Frame _frame;

    private final BasicBlock _block;

    public int _pc;        // program counter

    public Context(int pc, Frame frame, BasicBlock block) {
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
}
