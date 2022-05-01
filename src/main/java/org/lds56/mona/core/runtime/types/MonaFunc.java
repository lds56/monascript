package org.lds56.mona.core.runtime.types;

import org.lds56.mona.core.exception.InvokeErrorException;

public class MonaFunc extends MonaObject {

    public String getName() {
        return "unnamed";
    }

    @Override
    public MonaObject invoke(MonaObject... args) throws InvokeErrorException {
        throw new InvokeErrorException("Invocable logic not implemented");
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Function;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    // TODO: convert to invocable class
    @Override
    public Object getValue() {
        return null;
    }
}
