package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaUndefined;

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

    public MonaObject findGlobal(int negIndex) {
        Frame outer;;
        while ((outer = _frame.getOuter()) != null) {
            negIndex = outer.getLocalNum() + negIndex;
            if (negIndex >= 0) {
                return outer.getLocal(negIndex);
            }
        }
        return MonaUndefined.UNDEF;
    }

    public void setGlobal(int negIndex, MonaObject value) {
        Frame outer;;
        while ((outer = _frame.getOuter()) != null) {
            negIndex = outer.getLocalNum() + negIndex;
            if (negIndex >= 0) {
                outer.setLocal(negIndex, value);
                return;
            }
        }
    }
}
