package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaUndefined;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Basic Block
 *
 */
public class BasicBlock {

    public Info info;

    public Vars vars;

    private Instruction[] instructions;

    public BasicBlock(Info info, Vars vars, Instruction[] instructions) {
        this.info = info;
        this.vars = vars;
        this.instructions = instructions;
    }

    public static BasicBlock build(ByteCodeMetadata metadata, ByteCodeBlock block) {
        return BasicBlock.build(block.toInstrArray())
                         .info(metadata.getBlockName(), 0)
                         .vars(metadata.toConstArray(), metadata.toVarNameArray(),
                               metadata.toGlobalNameArray(), metadata.toGlobalPosArray());
    }

    public static BasicBlock build(Info info, Vars vars, Instruction[] instructions) {
        return new BasicBlock(info, vars, instructions);
    }

    public static BasicBlock build(Instruction[] instructions) {
        return new BasicBlock(null, null, instructions);
    }

    public BasicBlock info(String name, Integer startAddress) {
        this.info = new Info(name, startAddress);
        return this;
    }

    public BasicBlock vars(MonaObject[] constants, String[] localNames, String[] globalNames, Integer[] globalPos) {
        this.vars = new Vars(constants, localNames, globalNames, globalPos);
        return this;
    }

    public BasicBlock vars(MonaObject[] constants, String[] localNames) {
        this.vars = new Vars(constants, localNames, null, null);
        return this;
    }

    // getters
    public String name() {
        return info.name;
    }

    public Integer startAddress() {
        return info.startAddress;
    }

    public String getGlobalVarName(int index) {
        return vars.globalNames[index];
    }

    public Integer getGlobalPos(int index) {
        return vars.globalPos[index];
    }

    public String[] getLocalNames() {
        return vars.localNames;
    }

    public int getLocalNum() {
        return vars.localNames.length;
    }

    public Instruction instrAt(int line) {
        return instructions[line - info.startAddress];
    }

    public int instrCount() {
        return instructions.length;
    }

    public Instruction[] getInstructions() {
        return instructions;
    }


    // setters
    public void setStartAddress(int startAddress) {
        this.info.startAddress = startAddress;
    }

    public MonaObject loadConst(int index) {
        if (index < 0 || index >= vars.constants.length) {
            throw new InterpretErrorException("Load const value out of range");
        }
        return vars.constants[index];
    }

    public MonaObject[] fillLocals(Map<String, Object> inputs) {
        MonaObject[] locals = new MonaObject[vars.localNames.length];
        for (int i=0; i<locals.length; i++) {
            if (inputs.containsKey(vars.localNames[i])) {
                locals[i] = MonaObject.wrap(inputs.get(vars.localNames[i]));
            }
            else {
                locals[i] = MonaUndefined.UNDEF;
            }
        }
        return locals;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (vars.constants != null && vars.constants.length > 0)
            sb.append("CONST: [").append(Arrays.stream(vars.constants).map(MonaObject::toString).collect(Collectors.joining(","))).append("]\n");
        if (vars.localNames != null && vars.localNames.length > 0)
            sb.append("LOCAL: ").append(Arrays.toString(vars.localNames)).append("\n");
        if (instructions != null && instructions.length > 0) {
            for (Instruction instr : instructions) {
                sb.append(instr.toString()).append("\n");
            }
        }
        return sb.toString();
    }


    public static class Info {
        private String name;

        private Integer startAddress;

        public Info(String name, Integer startAddress) {
            this.name = name;
            this.startAddress = startAddress;
        }
    }

    public static class Vars {

        public MonaObject[] constants;

        public String[] localNames;

        public String[] globalNames;

        public Integer[] globalPos;

        public Vars(MonaObject[] constants, String[] localNames, String[] globalNames, Integer[] globalPos) {
            this.constants = constants;
            this.localNames = localNames;
            this.globalNames = globalNames;
            this.globalPos = globalPos;
        }
    }

}
