package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.exception.InterpretErrorException;
import org.lds56.mona.core.runtime.functions.MonaModule;

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

    private StaticArea staticArea;

    // private MonaObject[] mainGlobals;

    private final BasicBlock[] basicBlocks;

    public ByteCode(BasicBlock[] basicBlocks, StaticArea staticArea) {
        this.basicBlocks = basicBlocks;
        // this.mainGlobals = new MonaObject[basicBlocks[Constants.MAIN_BASIC_BLOCK_INDEX].getGlobalNum()];
        this.staticArea = staticArea;
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

    public static ByteCode load(BasicBlock[] basicBlocks, StaticArea staticArea) {
        return new ByteCode(basicBlocks, staticArea);
    }

    public static ByteCode load(BasicBlock[] basicBlocks) {
        return new ByteCode(basicBlocks, new StaticArea());
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

    public void fillStatic(MonaModule globalMod) {
        for (int i=0; i<staticArea.size(); i++) {
            staticArea.set(i, globalMod.getMember(staticArea.getName(i)));
        }
    }

    public StaticArea getStaticArea() {
        return staticArea;
    }

//    public MonaObject[] getMainGlobals() {
//        return mainGlobals;
//    }
//
//    public String[] getStaticNames() { return staticNames; }

}
