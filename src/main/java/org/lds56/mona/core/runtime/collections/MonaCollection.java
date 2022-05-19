package org.lds56.mona.core.runtime.collections;

import java.util.Iterator;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: This is description.
 */
public interface MonaCollection {

    Iterator<?> iterator();

    MonaIter iter();

    MonaCollType getCollType();
}
