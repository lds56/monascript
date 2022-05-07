package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

// TODO: change it
public class MonaBB extends MonaObject {

    private final int bbIndex;

    private Frame freeFrame;        // store free variables

    public MonaBB(Integer index) {
        this.bbIndex = index;
        this.freeFrame = null;
    }

    public MonaBB withFrame(Frame frame) {
        this.freeFrame = frame;
        return this;
    }

    public Frame getFreeFrame() {
        return freeFrame;
    }

    // TODO
    @Override
    public MonaType getMonaType() {
        return MonaType.JavaType;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public int intValue() {
        return bbIndex;
    }
}
