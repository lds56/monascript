package org.lds56.mona.core.runtime.traits;

import org.lds56.mona.core.exception.TraitBadCastException;
import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 20 May 2022
 * @Description: This is description.
 */
public class MonaTrait {

//    Invocable,
//    Iterable,
//    Indexable,
//    ;

    public static MonaInvocable cast2invocable(MonaObject o) {
        if (!(o instanceof MonaInvocable)) {
            throw new TraitBadCastException("Cannot cast to invocable object");
        }
        return (MonaInvocable) o;
    }

    public static MonaIterable cast2iterable(MonaObject o) {
        if (!(o instanceof MonaIterable)) {
            throw new TraitBadCastException("Cannot cast to iterable object");
        }
        return (MonaIterable) o;
    }

    public static MonaIndexable cast2indexable(MonaObject o) {
        if (!(o instanceof MonaIndexable)) {
            throw new TraitBadCastException("Cannot cast to indexable object");
        }
        return (MonaIndexable) o;
    }
}
