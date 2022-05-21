package org.lds56.mona.core.runtime.traits;

import org.lds56.mona.core.runtime.collections.MonaCollType;
import org.lds56.mona.core.runtime.collections.MonaIter;

import java.util.Iterator;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: This is description.
 */
public interface MonaIterable {

    Iterator<?> iterator();

    MonaIter iter();

    MonaCollType getCollType();
}
