package org.lds56.mona.core.interpreter;


import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author lds56
 * @date 2022/04/26
 * @description Byte Code Metadata
 *
 */
public class ByteCodeMetadata {

    public String blockName;

    private final List<MonaObject> constValues;

    private final List<String> localVarNames;

    private int loopNo = 0;
    private final Stack<Integer> loopNoStack = new Stack<>();

    private int condNo = 0;
    private final Stack<Integer> condNoStack = new Stack<>();

    // private final Stack<> loopBlocks;

    public ByteCodeMetadata(String blockName) {
        this.blockName = blockName;
        this.constValues = new ArrayList<>();
        this.localVarNames = new ArrayList<>();
        this.constValues.add(MonaNull.NIL);
        // this.loopBlocks = new Stack<>();
    }

    public Integer getConstIndex(MonaObject obj) {
        constValues.add(obj);
        return constValues.size() - 1;
    }

    public Integer getVarNameIndex(String varName) {
        int idx = localVarNames.indexOf(varName);
        if (idx != -1) {
            return idx;
        }
        localVarNames.add(varName);
        return localVarNames.size()-1;
    }

    // cond label
    public int handleCondFlag(int flag) {
        if (flag == 1) {
            condNoStack.push(condNo++);
        }
        else if (flag == -1) {
            return condNoStack.pop();
        }
        return condNoStack.peek();
    }

    public String getCondEndLabel(int flag) {

        return "COND_#" + handleCondFlag(flag) + "_END";
    }

    public String getCondElseLabel(int flag) {
        return "COND_#" + handleCondFlag(flag) + "_ELSE";
    }

    // loop label
    public String enterLoopLabel() {
        loopNoStack.push(loopNo++);
        return getLoopStartLabel();
    }

    public String exitLoopLabel() {
        String label = getLoopEndLabel();
        loopNoStack.pop();
        return label;
    }

    public String getLoopStartLabel() {
        return "LOOP_#" + loopNoStack.peek() + "_START";
    }

    public String getLoopEndLabel() {
        return "LOOP_#" + loopNoStack.peek() + "_END";
    }

    public String getBlockName() {
        return blockName;
    }

    public MonaObject[] toConstArray() {
        MonaObject[] constArray = new MonaObject[constValues.size()];
        return constValues.toArray(constArray);
    }

    public String[] toVarNameArray() {
        String[] varNameArray = new String[localVarNames.size()];
        return localVarNames.toArray(varNameArray);
    }

    public String[] toGlobalNameArray() {
        return null;
    }

    public Integer[] toGlobalPosArray() {
        return null;
    }
}
