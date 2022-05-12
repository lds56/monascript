package org.lds56.mona.script;

import org.lds56.mona.core.exception.MonaRuntimeException;
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

    public BytecodeScript(ByteCode bc) {
        vm = VirtualMachFactory.createVM();
        vm.load(bc);
    }

    @Override
    public Object execute() {
        return execute(new HashMap<>());
    }

    @Override
    public Object execute(Map<String, Object> context) {
        if (vm.reset()) {
            return vm.run(context).getValue();
        }
        throw new MonaRuntimeException("Failed to reset VM");
    }
}
