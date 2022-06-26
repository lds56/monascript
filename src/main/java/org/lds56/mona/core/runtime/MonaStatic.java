package org.lds56.mona.core.runtime;

import org.lds56.mona.core.runtime.functions.MonaFunction;
import org.lds56.mona.core.runtime.functions.MonaLibFunc;
import org.lds56.mona.core.runtime.functions.MonaModule;
import org.lds56.mona.core.runtime.types.MonaObject;
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
public class MonaStatic {

//    private static class GlobalObj {
//        MonaObject   obj;
//        String       name;
//        List<String> path;
//    }

    static {
        topModule = new MonaModule();
        packages = new HashSet<>();
        addLibFunctions();
    }

    private final static MonaModule topModule;

    private final static Set<String> packages;

    // private GlobalObj[] globals;

    public static MonaModule getModule() {
        return topModule;
    }

    public static boolean isPackage(String name) {
        return packages.contains(name);
    }

    public MonaStatic() {}

    public static MonaObject get(String name) {
        return topModule.getMember(name);
    }

    // TODO: optimize
    private static void addModule(String name, MonaModule mod) {
        topModule.addMember(name, mod);
    }

    private static void addLibFunc(MonaModule mod, MonaLibFunc func) {
        MonaFunction monaFunc = new MonaFunction(func);
        mod.addMember(monaFunc.getName(), monaFunc);
    }

    private static void addLibFunctions() {

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
