package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;

import java.io.InputStream;
import java.io.Serializable;

/**
 *
 * let a=2;
 * a = a + 3;
 * return a;
 * =======FRAME========
 * LOCALS:  ["a"]
 * CONSTS:  [3]
 * GLOBALS: [...]
 * =======INSTR========
 * LOAD_CONST   0
 * STORE_LOCAL  0
 * SELF_ADD
 * RET_VALUE
 *
 *
 */

/**
 * @author lds56
 * @date 2022/04/21
 * @description Byte Code
 *
 */
public class ByteCode implements Serializable {

    private final BasicBlock[] basicBlocks;

    public ByteCode(BasicBlock[] basicBlocks) {
        this.basicBlocks = basicBlocks;
    }

//    public ByteCode load(String codeStr) {
////        if (instructions == null || instructions.length <= 1) {
////            throw new MonaRuntimeException("Two or more instructions epected in instruction list");        // TODO: add new exception
////        }
////        return new ByteCode(instructions);
//        return new ByteCode();
//    }
//
    public static ByteCode loadStr(String codeStr) {
        return null;
    }

    public static ByteCode loadFile(String fname) {
        return null;
    }

    public static ByteCode loadBin(InputStream in) {
        return null;
    }

    public static ByteCode load(BasicBlock[] basicBlocks) {
        return new ByteCode(basicBlocks);
    }

    public BasicBlock getBlock(int index) {
        if (index < 0 || index >= basicBlocks.length) {
            throw new InterpretErrorException("Visit basic block out of bound");
        }
        return basicBlocks[index];
    }

    public BasicBlock[] getBasicBlocks() {
        return basicBlocks;
    }

}
