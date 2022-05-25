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

    public static class Var {
        public boolean isLocal = true;
        public int index = -1;
        public int pos = -1;
    };

    public String bbName;
    public int bbIndex;

    private final List<MonaObject> constValues;
    private final List<String> localVarNames;
    private final List<String> globalVarNames;
    private final List<Integer> globalVarPos;

    private int loopNo = 0;
    private final Stack<Integer> loopNoStack = new Stack<>();

    private int condNo = 0;
    private final Stack<Integer> condNoStack = new Stack<>();

    // private final Stack<> loopBlocks;

    public ByteCodeMetadata(String bbName, int bbIndex) {
        this.bbName = bbName;
        this.bbIndex = bbIndex;
        this.constValues = new ArrayList<>();
        this.localVarNames = new ArrayList<>();
        this.globalVarNames = new ArrayList<>();
        this.globalVarPos = new ArrayList<>();
        this.constValues.add(MonaNull.NIL);
        // this.loopBlocks = new Stack<>();
    }

    public Integer getConstIndex(MonaObject obj) {
        int idx = constValues.indexOf(obj);
        if (idx != -1) {
            return idx;
        }
        constValues.add(obj);
        return constValues.size() - 1;
    }

    public Integer newVarNameIndex(String varName) {
        localVarNames.add(varName);
        return localVarNames.size()-1;
    }

    public Integer getVarNameIndex(String varName) {
        return localVarNames.indexOf(varName);
    }

    public Integer getOrNewVarNameIndex(String varName) {
        int index = localVarNames.indexOf(varName);
        if (index >= 0) {
            return index;
        }
        return newVarNameIndex(varName);
    }

    public Integer getGlobalVarIndex(String varName) {
        return globalVarNames.indexOf(varName);
    }

    public Integer newGlobalVarIndex(String varName, Integer varPos) {
        globalVarNames.add(varName);
        globalVarPos.add(varPos);
        return globalVarNames.size()-1;
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

    public MonaObject[] toConstArray() {
        MonaObject[] constArray = new MonaObject[constValues.size()];
        return constValues.toArray(constArray);
    }

    public String[] toVarNameArray() {
        String[] varNameArray = new String[localVarNames.size()];
        return localVarNames.toArray(varNameArray);
    }

    public String[] toGlobalNameArray() {
        String[] varNameArray = new String[globalVarNames.size()];
        return globalVarNames.toArray(varNameArray);
    }

    public Integer[] toGlobalPosArray() {
        Integer[] posArray = new Integer[globalVarPos.size()];
        return globalVarPos.toArray(posArray);
    }
}
