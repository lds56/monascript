package org.lds56.mona.core.interpreter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lds56
 * @date 2022/04/27
 * @description Block for building bytecode
 *
 */
public class ByteCodeBlock {

    private final List<InstructionExt> instrList;

    public ByteCodeBlock() {
        this.instrList = new ArrayList<>();
    }

    public ByteCodeBlock(List<InstructionExt> instrList) {
        this.instrList = instrList;
    }

    public static ByteCodeBlock build() {
        return new ByteCodeBlock();
    }

    public static ByteCodeBlock build(InstructionExt instr) {
        ByteCodeBlock block = new ByteCodeBlock();
        block.instrList.add(instr);
        return block;
    }

    public ByteCodeBlock append(InstructionExt instr) {
        instrList.add(instr);
        return this;
    }

    public ByteCodeBlock append(ByteCodeBlock block) {
        if (block != null) {
            this.instrList.addAll(block.instrList);
        }
        return this;
    }

    public ByteCodeBlock append(List<ByteCodeBlock> blocks) {
        if (blocks != null) {
            blocks.forEach(this::append);
        }
        return this;
    }

    public InstructionExt instrAt(int idx) {
        return instrList.get(idx);
    }

    public int instrCount() {
        return instrList.size();
    }

    public Instruction[] toInstrArray() {

        // retrieve label map
        Map<String, Integer> label2line = new HashMap<>();
        for (int i=0; i<instrList.size(); i++) {
            if (instrList.get(i).isLineLabel()) {
                label2line.put(instrList.get(i).getLineLabel(), i-label2line.size());
            }
        }

        // map to standard instruction
        // filter out label
        Instruction[] instrArray = new Instruction[instrList.size() - label2line.size()];
        int idx = 0;
        for (InstructionExt ext : instrList) {
            if (ext.isLineLabel()) {
                // do nothing
            } else if (ext.hasJumpLabel()) {
                instrArray[idx++] = Instruction.of(ext.getOpCode(), label2line.get(ext.getJumpLabel()));
            } else {
                instrArray[idx++] = Instruction.of(ext.getOpCode(), ext.getArg());
            }
        }
        return instrArray;
    }
}
