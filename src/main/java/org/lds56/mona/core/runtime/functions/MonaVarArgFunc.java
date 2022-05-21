package org.lds56.mona.core.runtime.functions;

import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: This is description.
 */
public interface MonaVarArgFunc extends MonaLibFunc {

    default MonaObject call(MonaObject... args) {
        throw new IllegalArgumentException("Varidict arguments call is not supported");
    }
}
