package org.lds56.mona.core.runtime.types;

import java.util.*;

public class MonaColl extends MonaObject implements Iterable<MonaObject> {

    private final Collection<MonaObject> coll;

    private final MonaCollType collType;
    
    public MonaColl(Collection<MonaObject> coll, MonaCollType collType) {
        this.coll = coll;
        this.collType = collType;
    }
    
    public static MonaColl newList(MonaObject[] objects) {
        return new MonaColl(Arrays.asList(objects), MonaCollType.List);
    }
    
    public static MonaColl newSet(MonaObject[] objects) {
        Set<MonaObject> s = new HashSet<>();
        Collections.addAll(s, objects);
        return new MonaColl(s, MonaCollType.Set);
    }

//    public static MonaColl newDict(MonaObject[] objects) {
//        return new MonaColl(Set.of(objects));
//    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Collection;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Iterator<MonaObject> iterator() {
        return coll.iterator();
    }

    @Override
    public MonaIter iter() {
        return new MonaIter(coll.iterator());
    }

    @Override
    public Object getValue() {
        return coll;
    }
}
