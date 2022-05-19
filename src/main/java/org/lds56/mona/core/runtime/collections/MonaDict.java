package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.exception.MonaRuntimeException;
import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.*;

public class MonaDict extends MonaObject implements MonaCollection {

    private final Map<MonaObject, MonaTuple> dict;

    public MonaDict(Map<MonaObject, MonaTuple> dict) {
        this.dict = dict;
    }

    public static MonaDict newDict(List<MonaObject> keys, List<MonaObject> values) {
        if (keys.size() != values.size()) {
            throw new MonaRuntimeException("The number of keys and values not matched");
        }
        Map<MonaObject, MonaTuple> m = new HashMap<>();
        for (int i=0; i<keys.size(); i++) {
            m.put(keys.get(i), MonaTuple.newPair(keys.get(i), values.get(i)));
        }
        return new MonaDict(m);
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Collection;
    }

    @Override
    public MonaCollType getCollType() {
        return MonaCollType.Dict;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Iterator<MonaTuple> iterator() {
        return dict.values().iterator();
    }

    @Override
    public MonaIter iter() {
        return new MonaIter(iterator());
    }

    @Override
    public Object getValue() {
        return dict;
    }

    public Boolean contain(MonaObject key) {
        return dict.containsKey(key);
    }

    public MonaObject get(MonaObject key) {
        if (dict.containsKey(key)) {
            return dict.get(key).at(1);
        }
        return MonaNull.NIL;
    }
}
