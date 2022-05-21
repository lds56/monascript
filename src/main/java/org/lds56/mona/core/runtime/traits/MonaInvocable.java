package org.lds56.mona.core.runtime.traits;

import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: Invocable interface for MonaObject
 */
public interface MonaInvocable {
    MonaObject invoke(MonaObject[] args);
}
