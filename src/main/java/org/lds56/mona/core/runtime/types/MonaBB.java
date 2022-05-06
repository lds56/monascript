package org.lds56.mona.core.runtime.types;

// TODO: change it
public class MonaBB extends MonaObject {

    private int bbIndex;

    public MonaBB(Integer index) {
        this.bbIndex = index;
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
