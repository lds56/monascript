package org.lds56.mona.core.benchmark;

import org.lds56.mona.core.interpreter.*;
import org.lds56.mona.core.interpreter.ir.OpCode;
import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaNumber;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.util.TestUtils;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class ProfilerTest {

    public static void main(String[] args)  {

//        MonaEngine engine = new MonaEngine();
//
//        String expr = "1+1";
//        Object result = engine.execute(expr);
//
//        Assertions.assertEquals(result, 2);

        // b? 1+1 : 3
        Instruction[] ins = new Instruction[] {
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.BRANCH_FALSE, 6),
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE),
                Instruction.of(OpCode.LOAD_CONSTANT, 3),
                Instruction.of(OpCode.RETURN_VALUE)
        };

        MonaObject[] consts = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(1),        // TODO: add common-used number
                MonaNumber.newInteger(2),
                MonaNumber.newInteger(3)
        };

        String[] localNames = new String[] {"b"};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(ins).info("__main__", 0).vars(consts, localNames, new String[] {"b"}, new Integer[] {0})
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        for (int i=0; i<10000; i++) {
            vm.reset();
            MonaObject res = vm.run(TestUtils.inputOf("b", true));
        }
    }
//
//    @Test
//    public void testExpr() {
//
//        MonaEngine engine = new MonaEngine();
//
//        String expr = "(a+b*c/d-1>3)? a+b : a-b";
//        Object result = engine.execute(expr, TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4));
//
//        Assertions.assertEquals(result, -1);
//    }
}
