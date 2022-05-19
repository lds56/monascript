package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.Iterator;

public class MonaIter extends MonaObject implements Iterator<MonaObject> {

    private final Iterator<?> iter;

    public MonaIter(Iterator<?> iter) {
        this.iter = iter;
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Collection;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
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
