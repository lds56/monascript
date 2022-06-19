package org.lds56.mona.core.runtime.traits;

import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 28 May 2022
 * @Description: Member access interface
 */
public interface MonaAccessible {

    MonaObject getProperty(String name);

    MonaObject callMethod(String name, MonaObject... args);
}
