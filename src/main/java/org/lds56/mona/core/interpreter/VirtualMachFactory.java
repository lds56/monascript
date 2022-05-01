package org.lds56.mona.core.interpreter;

/**
 * @author lds56
 * @date 2022/04/20
 * @description Virtual Machine Factory
 *
 */
public class VirtualMachFactory {

    public static org.lds56.mona.core.interpreter.VirtualMach createVM() {
        org.lds56.mona.core.interpreter.VirtualMach vm = new org.lds56.mona.core.interpreter.VirtualMach();
        return vm;
    }
}
