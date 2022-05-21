package org.lds56.mona.core.runtime.functions;

import org.lds56.mona.core.exception.InvokeErrorException;
import org.lds56.mona.core.runtime.traits.MonaInvocable;
import org.lds56.mona.core.runtime.types.MonaNType;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaType;

/**
 * @Author: Rui Chen
 * @Date: 19 May 2022
 * @Description: Function wrapper for customized function
 */
public class MonaFunction extends MonaObject implements MonaInvocable {

    private final MonaLibFunc innerFunc;

    private MonaFunction(MonaLibFunc innerFunc) {
        this.innerFunc = innerFunc;
    }

    public static MonaFunction newFunc(MonaFixedArgFunc innerFunc) {
        return new MonaFunction(innerFunc);
    }

    public static MonaFunction newFunc(MonaVarArgFunc innerFunc) {
        return new MonaFunction(innerFunc);
    }

    public String getName() {
        return this.innerFunc.getPackage() + "." + this.innerFunc.getName();
    }

    @Override
    public MonaType getMonaType() {
        return MonaType.Function;
    }

    @Override
    public MonaNType getNType() {
        return MonaNType.E;
    }

    // TODO
    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public MonaObject invoke(MonaObject[] args) {

        if (innerFunc instanceof MonaVarArgFunc) {
            return ((MonaVarArgFunc) innerFunc).call(args);
        }

        if (innerFunc instanceof MonaFixedArgFunc) {
            MonaFixedArgFunc fixedInnerFunc = (MonaFixedArgFunc) innerFunc;
            switch (args.length) {
                case 0: return fixedInnerFunc.call();
                case 1: return fixedInnerFunc.call(args[0]);
                case 2: return fixedInnerFunc.call(args[0], args[1]);
                case 3: return fixedInnerFunc.call(args[0], args[1], args[2]);
                case 4: return fixedInnerFunc.call(args[0], args[1], args[2], args[3]);
                case 5: return fixedInnerFunc.call(args[0], args[1], args[2], args[3], args[4]);
                case 6: return fixedInnerFunc.call(args[0], args[1], args[2], args[3], args[4], args[5]);
                case 7: return fixedInnerFunc.call(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
                default: throw new IllegalArgumentException("Number of arg not matched");
            }
        }

        throw new InvokeErrorException("Inner function not found");

    }
}
