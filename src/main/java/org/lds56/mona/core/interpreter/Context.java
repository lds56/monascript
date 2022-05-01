package org.lds56.mona.core.interpreter;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Context when carrying out instruction
 *
 */
public class Context {

    private final org.lds56.mona.core.interpreter.Frame _frame;

    private final org.lds56.mona.core.interpreter.BasicBlock _block;

    public int _pc;        // program counter

    public Context(int pc, org.lds56.mona.core.interpreter.Frame frame, org.lds56.mona.core.interpreter.BasicBlock block) {
        this._pc = pc;
        this._frame = frame;
        this._block = block;
    }

    public int pc() { return _pc; }

    public org.lds56.mona.core.interpreter.Frame frame() {
        return _frame;
    }

    public org.lds56.mona.core.interpreter.BasicBlock block() {
        return _block;
    }
}
