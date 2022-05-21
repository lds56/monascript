package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.exception.OutOfBoundException;
import org.lds56.mona.core.runtime.types.MonaObject;

public class MonaIndexIter extends MonaIter {

    private final MonaObject[] data;

    private int next_index;

    public MonaIndexIter(MonaObject[] data) {
        this.data = data;
        this.next_index = 0;
    }

    @Override
    public Object getValue() {
        return data;
    }

    @Override
    public boolean hasNext() {
        return next_index < data.length;
    }

    @Override
    public MonaObject next() {
        if (!hasNext()) {
            throw new OutOfBoundException("Iterator has no next value");
        }
        return data[next_index++];
    }
}
