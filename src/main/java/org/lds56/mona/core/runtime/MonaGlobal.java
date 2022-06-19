package org.lds56.mona.core.runtime;

import org.lds56.mona.core.runtime.functions.MonaFunction;
import org.lds56.mona.core.runtime.functions.MonaLibFunc;
import org.lds56.mona.core.runtime.functions.MonaModule;
import org.lds56.mona.library.java.coll.NewDictFunction;
import org.lds56.mona.library.java.coll.NewListFunction;
import org.lds56.mona.library.java.coll.NewRangeFunction;
import org.lds56.mona.library.java.coll.NewSetFunction;
import org.lds56.mona.library.java.sys.FileReadFunction;
import org.lds56.mona.library.java.sys.PrintFunction;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: Rui Chen
 * @Date: 16 Jun 2022
 * @Description: This is description.
 */
public class MonaGlobal {

    private final MonaModule module = new MonaModule();

    private final Set<String> packages = new HashSet<>();

    public MonaModule getModule() {
        return module;
    }

    public boolean isPackage(String name) {
        return packages.contains(name);
    }

    public MonaGlobal() {
        addLibFunctions();
    }

    // TODO: optimize
    private void addModule(String name, MonaModule mod) {
        module.addMember(name, mod);
    }

    private void addLibFunc(MonaModule mod, MonaLibFunc func) {
        MonaFunction monaFunc = new MonaFunction(func);
        mod.addMember(monaFunc.getName(), monaFunc);
    }

    private void addLibFunctions() {

        MonaModule collMod = new MonaModule();
        addLibFunc(collMod, new NewListFunction());
        addLibFunc(collMod, new NewSetFunction());
        addLibFunc(collMod, new NewRangeFunction());
        addLibFunc(collMod, new NewDictFunction());
        addModule("coll", collMod);

        MonaModule sysMod = new MonaModule();
        addLibFunc(sysMod, new PrintFunction());
        addLibFunc(sysMod, new FileReadFunction());
        addModule("sys", sysMod);
    }
}
