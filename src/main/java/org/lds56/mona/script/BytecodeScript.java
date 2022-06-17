package org.lds56.mona.script;

import org.lds56.mona.core.interpreter.ByteCode;
import org.lds56.mona.core.interpreter.VirtualMach;
import org.lds56.mona.core.interpreter.VirtualMachFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class BytecodeScript implements MonaScript {

    private final VirtualMach vm;

    private final ByteCode bc;

    public BytecodeScript(ByteCode bc) {
        this.bc = bc;
        this.vm = VirtualMachFactory.createVM();
    }

//    public String export() {
//
//    }
//
//    public static BytecodeScript load(String ) {
//
//    }

    @Override
    public Object execute() {
        return execute(new HashMap<>());
    }

    @Override
    public Object execute(Map<String, Object> context) {
        vm.load(bc);
        // if (vm.reset()) {
        return vm.run(context).getValue();
        // }
    }
}
