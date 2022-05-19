package org.lds56.mona.core.runtime.collections;

import org.lds56.mona.core.exception.OutOfBoundException;
import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

import java.util.List;

public class MonaTuple extends MonaObject implements MonaCollection {

    private final MonaObject[] value;

    public MonaTuple(MonaObject... items) {
        this.value = items;
    }

    public static MonaTuple newSingle(MonaObject item) {
        return new MonaTuple(item);
    }

    public static MonaTuple newPair(MonaObject first, MonaObject second) {
        return new MonaTuple(first, second);
    }

    public static MonaTuple newTuple(MonaObject... items) {
        return new MonaTuple(items);
    }

    public static MonaTuple fromList(List<MonaObject> items) {
        MonaObject[] arr = new MonaObject[items.size()];
        for (int i=0; i<items.size(); i++) {
            arr[i] = items.get(i);
        }
        return new MonaTuple(arr);
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Tuple;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public MonaCollType getCollType() {
        return MonaCollType.Tuple;
    }

    @Override
    public Object getValue() {
        return value;
    }

    public MonaObject[] toArray() {
        return value;
    }

    public int length() {
        return value == null? 0 : value.length;
    }

    public MonaObject at(int idx) {
        if (idx < 0 || idx >= value.length) {
            throw new OutOfBoundException("Out of bound");
        }
        return value[idx];
    }
}
