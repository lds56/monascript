package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.runtime.types.MonaObject;

import java.util.Iterator;

public class MonaCollIter extends MonaIter {

    private final Iterator<?> iter;

    public MonaCollIter(Iterator<?> iter) {
        this.iter = iter;
    }

    @Override
    public Object getValue() {
        return iter;
    }

    @Override
    public boolean hasNext() {
        return iter.hasNext();
    }

    @Override
    public MonaObject next() {
        return MonaObject.wrap(iter.next());
    }
}
