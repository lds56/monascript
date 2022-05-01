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

    public String name;

    public int startAddress;

    private Instruction[] instructions;

    private MonaObject[] constValues;

    private String[] localVarNames;

    public BasicBlock(int startAddress, Instruction[] instructions, MonaObject[] constValues, String[] localVarNames) {
        new BasicBlock(null, startAddress, instructions, constValues, localVarNames);
    }

    public BasicBlock(String name, int startAddress, Instruction[] instructions, MonaObject[] constValues, String[] localVarNames) {
        this.name = name;
        this.startAddress = startAddress;
        this.instructions = instructions;
        this.constValues = constValues;
        this.localVarNames = localVarNames;
    }

    public static BasicBlock build(ByteCodeMetadata metadata, ByteCodeBlock block) {
        return new BasicBlock(metadata.getBlockName(), 0, block.toInstrArray(), metadata.toConstArray(), metadata.toVarNameArray());
    }


    public static BasicBlock build(int startAddress, Instruction[] instructions, MonaObject[] constValues, String[] localVarNames) {
        return new BasicBlock(startAddress, instructions, constValues, localVarNames);
    }

    public void setStartAddress(int startAddress) {
        this.startAddress = startAddress;
    }

    public BasicBlock withName(String name) {
        this.name = name;
        return this;
    }

    public MonaObject loadConst(int index) {
        if (index < 0 || index >= constValues.length) {
            throw new InterpretErrorException("Load const value out of range");
        }
        return constValues[index];
    }

    public MonaObject[] fillLocals(Map<String, Object> inputs) {
        MonaObject[] locals = new MonaObject[localVarNames.length];
        for (int i=0; i<locals.length; i++) {
            if (inputs.containsKey(localVarNames[i])) {
                locals[i] = MonaObject.wrap(inputs.get(localVarNames[i]));
            }
            else {
                locals[i] = MonaUndefined.UNDEF;
            }
        }
        return locals;
    }

    public int getLocalNum() {
        return localVarNames.length;
    }

    public Instruction instrAt(int line) {
        return instructions[line - startAddress];
    }

    public int instrCount() {
        return instructions.length;
    }

    public Instruction[] getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CONST: ").append(Arrays.stream(constValues).map(o -> o.getValue().toString()).collect(Collectors.joining(","))).append("\n");
        sb.append("LOCAL: ").append(Arrays.toString(localVarNames)).append("\n");
        for (Instruction instr : instructions) {
            sb.append(instr.toString()).append("\n");
        }
        return sb.toString();
    }
}
