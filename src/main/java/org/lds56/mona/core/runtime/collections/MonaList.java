package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.exception.OutOfBoundException;
import org.lds56.mona.core.runtime.traits.MonaIndexable;
import org.lds56.mona.core.runtime.traits.MonaIterable;
import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.*;

public class MonaList extends MonaObject implements MonaIterable, MonaIndexable {

    private final List<MonaObject> list;

    public MonaList(List<MonaObject> list) {
        this.list = list;
    }
    
    public static MonaList newList(MonaObject... objects) {
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
        return new MonaCollIter(list.iterator());
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

    @Override
    public MonaObject index(MonaObject index) {
        return at(index.intValue());
    }

    @Override
    public String toString() {
        return "list" + list.toString();
    }

    // public MonaObject get()
}
