package org.lds56.mona.core.interpreter.ir;

import org.lds56.mona.core.interpreter.Context;

@FunctionalInterface
public interface IREval {
    Signal eval(Context context, Integer arg);
}
