package org.lds56.mona.core.runtime.functions;

import org.lds56.mona.core.exception.InvalidArgumentException;
import org.lds56.mona.core.runtime.traits.MonaAccessible;
import org.lds56.mona.core.runtime.traits.MonaInvocable;
import org.lds56.mona.core.runtime.traits.MonaTrait;
import org.lds56.mona.core.runtime.types.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 19 Jun 2022
 * @Description: This is description.
 */
public class MonaModule extends MonaObject implements MonaAccessible {

    private final Map<String, MonaObject> members;

    public MonaModule() {
        members = new HashMap<>();
    }

    public void addMember(String name, MonaObject member) {
        members.put(name, member);
    }

    public MonaObject getMember(String name) {
        return members.getOrDefault(name, MonaUndefined.UNDEF);
    }

    @Override
    public MonaObject getProperty(String name) {
        if (!members.containsKey(name)) {
            throw new InvalidArgumentException(" No such a member named " + name);
        }
        return members.get(name);
    }

    @Override
    public MonaObject callMethod(String name, MonaObject... args) {
        if (!members.containsKey(name)) {
            throw new InvalidArgumentException(" No such a method named " + name);
        }
        MonaInvocable method = MonaTrait.cast(members.get(name), MonaInvocable.class);
        return method.invoke(args);
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Module;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    @Override
    public Object getValue() {
        // TODO
        return null;
    }
}
