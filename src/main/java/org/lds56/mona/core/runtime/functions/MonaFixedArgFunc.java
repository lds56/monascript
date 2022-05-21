package org.lds56.mona.core.runtime.functions;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: This is description.
 */

import org.lds56.mona.core.exception.InvalidArgumentException;
import org.lds56.mona.core.runtime.types.MonaObject;

public interface MonaFixedArgFunc extends MonaLibFunc {

    default MonaObject call() {
        throw new InvalidArgumentException("No arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0) {
        throw new InvalidArgumentException("1 arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1) {
        throw new InvalidArgumentException("2 args call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1, MonaObject arg2) {
        throw new InvalidArgumentException("3 arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1, MonaObject arg2, MonaObject arg3) {
        throw new InvalidArgumentException("4 arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1, MonaObject arg2, MonaObject arg3, MonaObject arg4) {
        throw new InvalidArgumentException("5 arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1, MonaObject arg2, MonaObject arg3, MonaObject arg4, MonaObject arg5) {
        throw new InvalidArgumentException("No arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1, MonaObject arg2, MonaObject arg3, MonaObject arg4, MonaObject arg5, MonaObject arg6) {
        throw new InvalidArgumentException("No arg call is not supported.");
    }

    default MonaObject call(MonaObject arg0, MonaObject arg1, MonaObject arg2, MonaObject arg3, MonaObject arg4, MonaObject arg5, MonaObject arg6, MonaObject arg7) {
        throw new InvalidArgumentException("No arg call is not supported.");
    }


}
