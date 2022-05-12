package org.lds56.mona.core.interpreter;

import org.lds56.mona.engine.EngineOption;
import org.lds56.mona.engine.MonaEngine;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Virtual Machine Factory
 *
 */
public class VirtualMachFactory {

    public static VirtualMach createVM() {

        boolean printTraceInfo = MonaEngine.getEngineMode()
                                           .getEnabled(EngineOption.IR_CODE_TRACE_STACK_PRINT_SWITCH);

        return new VirtualMach(printTraceInfo);
    }
}
