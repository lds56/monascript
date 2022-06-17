package org.lds56.mona.core.runtime;

import org.lds56.mona.core.runtime.functions.MonaFunction;
import org.lds56.mona.core.runtime.functions.MonaLibFunc;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.library.java.coll.NewDictFunction;
import org.lds56.mona.library.java.coll.NewListFunction;
import org.lds56.mona.library.java.coll.NewRangeFunction;
import org.lds56.mona.library.java.coll.NewSetFunction;
import org.lds56.mona.library.java.sys.PrintFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 16 Jun 2022
 * @Description: This is description.
 */
public class MonaGlobal {

    private final Map<String, MonaObject> globals = new HashMap<>();

    public Map<String, MonaObject> getGlobals() {
        return globals;
    }

    public MonaGlobal() {
        addLibFunctions();
    }

    private void addLibFunc(MonaLibFunc func) {
        MonaFunction monaFunc = new MonaFunction(func);
        globals.put(monaFunc.getName(), monaFunc);
    }

    private void addLibFunctions() {
        addLibFunc(new NewDictFunction());
        addLibFunc(new NewListFunction());
        addLibFunc(new NewRangeFunction());
        addLibFunc(new NewSetFunction());
        addLibFunc(new PrintFunction());
    }
}
