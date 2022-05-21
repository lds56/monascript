package org.lds56.mona.library.java.coll;

import org.lds56.mona.core.runtime.collections.MonaDict;
import org.lds56.mona.core.runtime.functions.MonaVarArgFunc;
import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: This is description.
 */
public class NewDictFunction implements MonaVarArgFunc {

    @Override
    public String getName() {
        return "dict";
    }

    @Override
    public String getPackage() {
        return "coll";
    }

    @Override
    public MonaObject call(MonaObject... args) {
        return MonaDict.newDict(args);
    }
}
