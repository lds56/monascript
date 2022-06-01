//package org.lds56.mona.core.runtime.types;
//
//import org.lds56.mona.core.code.base.MonaCallable;
//import org.lds56.mona.core.env.MonaEnv;
//import org.lds56.mona.core.exception.InvalidArgumentException;
//import org.lds56.mona.core.exception.InvokeErrorException;
//
///**
// * @author chenrui
// * @date 2022/04/01
// * @desc Lambda type belong to mona type system
// */
//public class MonaLambda extends MonaObject {
//
//    private final MonaEnv parentEnv;
//
//    private final MonaCallable caller;
//
//    private final String[] vars;
//
//    private final int argNum;
//
//    public MonaLambda(MonaEnv parentEnv, String[] vars, int argNum, MonaCallable caller) {
//        this.parentEnv = parentEnv;
//        this.vars = vars;
//        this.caller = caller;
//        this.argNum = argNum;
//    }
//
//    @Override
//    public MonaObject invoke(MonaObject[] args) throws InvokeErrorException {
//
//        // argument number check
//        if (args.length != argNum) {
//            throw new InvalidArgumentException("Number of arguments not matched, " +
//                                                 argNum + " expected but " + args.length + " got");
//        }
//
//        // init local env
//        MonaEnv env0 = MonaEnv.newLocal(parentEnv, vars);
//        for (int i=0; i<args.length; i++) {
//            env0.setVar(i, args[i]);
//        }
//
//        // call caller
//        return caller.call(env0);
//    }
//
//    @Override
//    public MonaType getMonaType() {
//        return MonaType.Lambda;
//    }
//
//    @Override
//    public MonaNType getNType() {
//        return MonaNType.E;
//    }
//
//    // TODO: convert to invocable class
//    @Override
//    public Object getValue() {
//        return null;
//    }
//}
