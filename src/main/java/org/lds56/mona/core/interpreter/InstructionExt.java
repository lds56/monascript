package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.interpreter.ir.OpCode;

/**
 * @author lds56
 * @date 2022/04/17
 * @description Instruction with label extension
 *
 */
public class InstructionExt extends Instruction {

    private String jumpLabel = null;

    private String lineLabel = null;

    public InstructionExt(OpCode opCode, Integer arg) {
        super(opCode, arg);
    }

    public InstructionExt(OpCode opCode, Integer arg, String jumpLabel, String lineLabel) {
        super(opCode, arg);
        this.jumpLabel = jumpLabel;
        this.lineLabel = lineLabel;
    }

    public static InstructionExt of(OpCode opCode) {
        return new InstructionExt(opCode, null, null, null);
    }

    public static InstructionExt of(OpCode opCode, Integer arg) {
        return new InstructionExt(opCode, arg, null, null);
    }

    public static InstructionExt of(OpCode opCode, String label) {
        return new InstructionExt(opCode, null, label, null);
    }

    public static InstructionExt labelOf(String lineLabel) {
        return new InstructionExt(null, null, null, lineLabel);
    }

    public boolean isLineLabel() {
        return this.lineLabel != null;
    }

    public boolean hasJumpLabel() {
        return this.jumpLabel != null;
    }

    public String getLineLabel() {
        return this.lineLabel;
    }

    public String getJumpLabel() {
        return this.jumpLabel;
    }

    @Override
    public String toString() {
        if (lineLabel != null) {
            return "[" + lineLabel + "]:";
        }
        if (jumpLabel != null) {
            return super.toString() + " [" + jumpLabel + "]";
        }
        return super.toString();
    }

}
