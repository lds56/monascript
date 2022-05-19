package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.exception.OutOfBoundException;
import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.*;

public class MonaList extends MonaObject implements MonaCollection {

    private final List<MonaObject> list;

    public MonaList(List<MonaObject> list) {
        this.list = list;
    }
    
    public static MonaList newList(MonaObject[] objects) {
        return new MonaList(Arrays.asList(objects));
    }

    public static MonaList newList(List<MonaObject> objects) {
        return new MonaList(objects);
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Collection;
    }

    @Override
    public MonaCollType getCollType() {
        return MonaCollType.List;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Iterator<MonaObject> iterator() {
        return list.iterator();
    }

    @Override
    public MonaIter iter() {
        return new MonaIter(list.iterator());
    }

    @Override
    public Object getValue() {
        return list;
    }

    public MonaObject at(int idx) {
        if (idx < 0 || idx >= list.size()) {
            throw new OutOfBoundException();
        }
        return list.get(idx);
    }

    // public MonaObject get()
}
