package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.*;

public class MonaSet extends MonaObject implements MonaCollection {

    private final Set<MonaObject> set;

    public MonaSet(Set<MonaObject> set) {
        this.set = set;
    }

    public static MonaSet newSet(MonaObject[] objects) {
        Set<MonaObject> s = new HashSet<>();
        Collections.addAll(s, objects);
        return new MonaSet(s);
    }

    public static MonaSet newSet(List<MonaObject> objects) {
        return new MonaSet(new HashSet<>(objects));
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
    public MonaCollType getCollType() {
        return MonaCollType.Set;
    }

    @Override
    public Iterator<MonaObject> iterator() {
        return set.iterator();
    }

    @Override
    public MonaIter iter() {
        return new MonaIter(set.iterator());
    }


    @Override
    public Object getValue() {
        return set;
    }

    public Boolean contain(MonaObject key) {
        return set.contains(key);
    }

    // public MonaObject get()
}
