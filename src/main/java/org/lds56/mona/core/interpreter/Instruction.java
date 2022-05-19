package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.interpreter.ir.OpCode;
import org.lds56.mona.core.interpreter.ir.Signal;

/**
 * @author lds56
 * @date 2022/04/17
 * @description Instruction combined with op code and argument
 *
 */
public class Instruction {

    private final OpCode opCode;

    private final Integer arg;

    public Instruction(OpCode opCode, Integer arg) {
        this.opCode = opCode;
        this.arg = arg;
    }

    public static Instruction of(OpCode opCode) {
        return new Instruction(opCode, null);
    }

    public static Instruction of(OpCode opCode, Integer arg) {
        return new Instruction(opCode, arg);
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public Integer getArg() {
        return arg;
    }

    public Signal visit(Context context) {
        return opCode.apply(context, arg);
    }

    @Override
    public String toString() {
        if (arg != null) {
            return opCode.repr() + " " + arg;
        }
        return opCode.repr();
    }
}
