package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.exception.MonaRuntimeException;
import org.lds56.mona.core.exception.TraitBadCastException;
import org.lds56.mona.core.runtime.traits.MonaHashable;
import org.lds56.mona.core.runtime.traits.MonaIndexable;
import org.lds56.mona.core.runtime.traits.MonaIterable;
import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.*;

public class MonaDict extends MonaObject implements MonaIterable, MonaIndexable {

    private final Map<MonaObject, MonaTuple> dict;

    public MonaDict() {
        this.dict = new HashMap<>();
    }

    public MonaDict(Map<MonaObject, MonaTuple> dict) {
        this.dict = dict;
    }

    public static MonaDict newDict(List<MonaObject> kvList) {
        if (kvList.size() % 2 != 0) {
            throw new MonaRuntimeException("The number of keys and values not matched");
        }
        MonaDict dict = new MonaDict();
        for (int i=0; i<kvList.size()/2; i++) {
            dict.add(kvList.get(2*i), kvList.get(2*i+1));
        }
        return dict;
    }

    public static MonaDict newDict(MonaObject... kvList) {
        if (kvList.length % 2 != 0) {
            throw new MonaRuntimeException("The number of keys and values not matched");
        }
        MonaDict dict = new MonaDict();
        Map<MonaObject, MonaTuple> m = new HashMap<>();
        for (int i=0; i<kvList.length/2; i++) {
            dict.add(kvList[2*i], kvList[2*i+1]);
        }
        return dict;
    }

//    public static MonaDict newDict(List<MonaObject> keys, List<MonaObject> values) {
//        if (keys.size() != values.size()) {
//            throw new MonaRuntimeException("The number of keys and values not matched");
//        }
//        Map<MonaObject, MonaTuple> m = new HashMap<>();
//        for (int i=0; i<keys.size(); i++) {
//            m.put(keys.get(i), MonaTuple.newPair(keys.get(i), values.get(i)));
//        }
//        return new MonaDict(m);
//    }

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
        return new MonaCollIter(iterator());
    }

    @Override
    public Object getValue() {
        return dict;
    }

    public void add(MonaObject key, MonaObject value) {
        if (!(key instanceof MonaHashable)) {
            throw new TraitBadCastException("The key of `dict` must be hashable");
        }
        dict.put(key, MonaTuple.newPair(key, value));
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

    @Override
    public MonaObject index(MonaObject index) {
        return get(index);
    }

    @Override
    public String toString() {
        return "dict" + dict.values().toString();
    }

}
