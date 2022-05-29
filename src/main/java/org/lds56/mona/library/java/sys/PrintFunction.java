package org.lds56.mona.library.java.sys;

import org.lds56.mona.core.runtime.functions.MonaVarArgFunc;
import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 24 May 2022
 * @Description: Print function
 */
public class PrintFunction  implements MonaVarArgFunc  {

    @Override
    public String getName() {
        return "print";
    }

    @Override
    public String getPackage() {
        return "sys";
    }

    @Override
    public MonaObject call(MonaObject... args) {
        for (int i=0; i<args.length; i++) {
            System.out.print(args[i]);
            if (i == args.length-1) {
                System.out.print(" ");
            }
            else {
                System.out.println();
            }
        }
        return MonaNull.NIL;
    }
}
