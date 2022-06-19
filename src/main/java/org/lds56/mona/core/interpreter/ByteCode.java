package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;
import org.lds56.mona.core.runtime.functions.MonaModule;
import org.lds56.mona.core.runtime.types.MonaObject;

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

    private MonaObject[] mainGlobals;

    private final BasicBlock[] basicBlocks;

    public ByteCode(BasicBlock[] basicBlocks) {
        this.basicBlocks = basicBlocks;
        this.mainGlobals = new MonaObject[basicBlocks[Constants.MAIN_BASIC_BLOCK_INDEX].getGlobalNum()];
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

    public BasicBlock getMainBlock() {
        return getBlock(Constants.MAIN_BASIC_BLOCK_INDEX);
    }

    public BasicBlock[] getBasicBlocks() {
        return basicBlocks;
    }

    public void fillMainGlobals(MonaModule globalMod) {
        String[] mainGlobalNames = basicBlocks[Constants.MAIN_BASIC_BLOCK_INDEX].getGlobalNames();
        for (int i=0; i<mainGlobals.length; i++) {
            mainGlobals[i] = globalMod.getMember(mainGlobalNames[i]);
        }
    }

    public MonaObject[] getMainGlobals() {
        return mainGlobals;
    }

}
