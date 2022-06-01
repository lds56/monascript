package org.lds56.mona.library.java.coll;

import org.lds56.mona.core.runtime.collections.MonaList;
import org.lds56.mona.core.runtime.functions.MonaFixedArgFunc;
import org.lds56.mona.core.runtime.types.MonaNumber;
import org.lds56.mona.core.runtime.types.MonaObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: This is description.
 */
public class NewRangeFunction implements MonaFixedArgFunc {

    @Override
    public String getName() {
        return "range";
    }

    @Override
    public String getPackage() {
        return "coll";
    }

    @Override
    public MonaObject call(MonaObject start, MonaObject end) {
        int startIndex = start.intValue();
        int endIndex = end.intValue();
        List<MonaObject> l = new ArrayList<>();
        for (int i=startIndex; i<endIndex; i++) {
            l.add(MonaNumber.newInteger(i));
        }
        return MonaList.newList(l);
    }
}
