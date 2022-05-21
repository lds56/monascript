package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.Iterator;

public abstract class MonaIter extends MonaObject implements Iterator<MonaObject> {

    @Override
    public MonaType getMonaType() {
        return MonaType.Iter;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

}
