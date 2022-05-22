package org.lds56.mona.core.interpreter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.interpreter.ir.OpCode;
import org.lds56.mona.core.runtime.types.MonaNull;
import org.lds56.mona.core.runtime.types.MonaNumber;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.runtime.types.MonaString;
import org.lds56.mona.core.util.TestUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ByteCodeTest {

    @Test
    public void testExpr() {

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
        MonaObject res = vm.run(TestUtils.inputOf("b", true));

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 2);
    }

    @Test
    public void testFuncCall() {

        // fn f(x) {
        //    x+1
        //}
        //f(y)
        Instruction[] ins_main = new Instruction[] {
                // Instruction.of(OpCode.LOAD_CONSTANT, 2),    // bb index
                // Instruction.of(OpCode.LOAD_CONSTANT, 3),
                Instruction.of(OpCode.MAKE_FUNCTION, 1),
                Instruction.of(OpCode.STORE_LOCAL, 0),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.CALL_FUNCTION, 1),
                Instruction.of(OpCode.RETURN_VALUE)
                // [Nil, 1, 'f', 2]
        };
        MonaObject[] consts_main = new MonaObject[] {
                MonaNull.NIL,
                MonaString.valueOf("f"),
                // MonaNumber.newInteger(1),        // bb index
        };

        String[] localNames_main = new String[] {"f"};

        Instruction[] ins_f = new Instruction[] {
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE)
        };

        // [Nil, 1, 'f', 2]
        MonaObject[] consts_f = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(1)
        };

        String[] localNames_f = new String[] {"x"};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(ins_main).info("__f__", 0).vars(consts_main, localNames_main, new String[]{"y"}, new Integer[]{0}),
                BasicBlock.build(ins_f).info("__main__", ins_main.length).vars(consts_f, localNames_f),
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        MonaObject res = vm.run(TestUtils.inputOf("y", 10));

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 11);
    }

    @Test
    public void testFuncCall2() {

//        let counter = 1;
//        let adder = x -> {
//            counter += 1;
//            return x + counter;
//        }
//        return adder(1) + adder(1);
        Instruction[] ins_main = new Instruction[] {
                // Instruction.of(OpCode.LOAD_CONSTANT, 2),    // bb index
                // Instruction.of(OpCode.LOAD_CONSTANT, 3),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.STORE_LOCAL,      0),
                Instruction.of(OpCode.MAKE_FUNCTION,    1),
                Instruction.of(OpCode.STORE_LOCAL,      1),
                Instruction.of(OpCode.LOAD_LOCAL,       1),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.CALL_FUNCTION,    1),
                Instruction.of(OpCode.LOAD_LOCAL,       1),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.CALL_FUNCTION,    1),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE)
                // [Nil, 1, 'f', 2]
        };

        MonaObject[] consts_main = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.valueOf(1),
        };

        String[] localNames_main = new String[] {"counter", "adder"};

        Instruction[] ins_f = new Instruction[] {
                Instruction.of(OpCode.LOAD_GLOBAL,      0),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.INPLACE_ADD),
                Instruction.of(OpCode.STORE_GLOBAL,     0),
                Instruction.of(OpCode.LOAD_LOCAL,       0),
                Instruction.of(OpCode.LOAD_GLOBAL,      0),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE)
        };

        // [Nil, 1, 'f', 2]
        MonaObject[] consts_f = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(1)
        };

        String[] localNames_f = new String[] {"x"};

        String[] globalNames_f = new String[] {"counter"};

        Integer[] globalPos_f = new Integer[] {0};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(ins_main).info("__main__", 0).vars(consts_main, localNames_main),
                BasicBlock.build(ins_f).info("__f1__", ins_main.length).vars(consts_f, localNames_f, globalNames_f, globalPos_f),
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        MonaObject res = vm.run(TestUtils.inputOf());

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 7);
    }

    @Test
    public void testWhile() {

        // let x = 0;
        //while x<y {
        //    x += 1;
        //}
        //return x;
        Instruction[] ins = new Instruction[] {
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.STORE_LOCAL, 0),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.LESS_THAN),
                Instruction.of(OpCode.BRANCH_FALSE, 11),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_CONSTANT, 2),
                Instruction.of(OpCode.INPLACE_ADD),
                Instruction.of(OpCode.STORE_LOCAL, 0),
                Instruction.of(OpCode.JUMP_LOCAL, 2),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.RETURN_VALUE),
        };

        MonaObject[] consts = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(0),
                MonaNumber.newInteger(1),
        };

        String[] localNames = new String[] {"x"};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(ins).info("__main__", 0).vars(consts, localNames, new String[]{"y"}, new Integer[]{0})
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        MonaObject res = vm.run(TestUtils.inputOf("y", 10));

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 10);
    }


    @Test
    public void testForIn() {

//        let ans = 0;
//        for x in l {
//            if x % 2 == 1 {
//                continue;
//            }
//            if x > 10 {
//                break;
//            }
//            ans += x;
//        }
//        return ans;
        Instruction[] ins = new Instruction[] {
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.STORE_LOCAL, 0),
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.GET_ITERATOR),
                Instruction.of(OpCode.NEXT_ITERATOR),
                Instruction.of(OpCode.BRANCH_FALSE, 20),
                Instruction.of(OpCode.STORE_LOCAL, 1),
                Instruction.of(OpCode.LOAD_LOCAL, 1),
                Instruction.of(OpCode.LOAD_CONSTANT, 2),
                Instruction.of(OpCode.BINARY_MODULO),
                Instruction.of(OpCode.LOAD_CONSTANT, 3),
                Instruction.of(OpCode.BRANCH_EQUAL, 4),
                Instruction.of(OpCode.LOAD_LOCAL, 1),
                Instruction.of(OpCode.LOAD_CONSTANT, 4),
                Instruction.of(OpCode.BRANCH_GREATER_THAN, 20),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_LOCAL, 1),
                Instruction.of(OpCode.INPLACE_ADD),
                Instruction.of(OpCode.STORE_LOCAL, 0),
                Instruction.of(OpCode.JUMP_LOCAL, 4),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.RETURN_VALUE),
                };

        MonaObject[] consts = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(0),
                MonaNumber.newInteger(2),
                MonaNumber.newInteger(1),
                MonaNumber.newInteger(10),
                };

        String[] localNames = new String[] {"ans", "x"};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(ins).info("__main__", 0).vars(consts, localNames, new String[]{"l"}, new Integer[]{0})
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        List<Integer> l =  Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).collect(Collectors.toList());
        MonaObject res = vm.run(TestUtils.inputOf("l", MonaObject.wrap(l)));

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 30);
    }

    @Test
    public void testRecursion() {
//        fn fib(n) {
//        if n == 1 || n == 2 {
//            return 1;
//        }
//        return fib(n-1) + fib(n-2);
//}
//        return fib(n);
        Instruction[] main_instr = new Instruction[] {
                Instruction.of(OpCode.MAKE_FUNCTION, 1),
                Instruction.of(OpCode.STORE_LOCAL, 0),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.CALL_FUNCTION, 1),
                Instruction.of(OpCode.RETURN_VALUE),
        };

        MonaObject[] main_const = new MonaObject[] {
                MonaNull.NIL,
        };

        String[] main_vars = new String[] { "fib", "n" };

        Instruction[] fib_instr = new Instruction[] {
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.EQUAL),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_CONSTANT, 2),
                Instruction.of(OpCode.EQUAL),
                Instruction.of(OpCode.LOGIC_OR),
                Instruction.of(OpCode.BRANCH_FALSE, 10),
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.RETURN_VALUE),
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_CONSTANT, 1),
                Instruction.of(OpCode.BINARY_SUBSTRACT),
                Instruction.of(OpCode.CALL_FUNCTION, 1),
                Instruction.of(OpCode.LOAD_GLOBAL, 0),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.LOAD_CONSTANT, 2),
                Instruction.of(OpCode.BINARY_SUBSTRACT),
                Instruction.of(OpCode.CALL_FUNCTION, 1),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE),
        };

        MonaObject[] fib_consts = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(1),
                MonaNumber.newInteger(2),
        };

        String[] fib_locals = new String[] {"n"};

        String[] fib_globals = new String[] {"fib"};

        Integer[] fib_pos = new Integer[] {0};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(main_instr).info("__main__", 0).vars(main_const, main_vars, new String[]{"n"}, new Integer[]{0}),
                BasicBlock.build(fib_instr).info("__fib__", main_instr.length).vars(fib_consts, fib_locals, fib_globals, fib_pos),
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        MonaObject res = vm.run(TestUtils.inputOf("n", 10));

        System.out.println(res.getValue());
        // 1 1 2 3 5 8 13 21 34 55
        Assertions.assertEquals(res.getValue(), 55);
    }

    @Test
    public void testClosure() {

//        fn addgen(x) {
//            fn adder(y) {
//                return x+y;
//            }
//                return adder;
//        }
//        let adder1 = addgen(1);
//        let adder2 = addgen(2);
//        return adder1(1) + adder2(2)

        ////////////////// main //////////////////
        Instruction[] main_instr = new Instruction[] {
                Instruction.of(OpCode.MAKE_FUNCTION,    1),
                Instruction.of(OpCode.STORE_LOCAL,      0),
                Instruction.of(OpCode.LOAD_LOCAL,       0),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.CALL_FUNCTION,    1),
                Instruction.of(OpCode.STORE_LOCAL,      1),
                Instruction.of(OpCode.LOAD_LOCAL,       0),
                Instruction.of(OpCode.LOAD_CONSTANT,    2),
                Instruction.of(OpCode.CALL_FUNCTION,    1),
                Instruction.of(OpCode.STORE_LOCAL,      2),
                Instruction.of(OpCode.LOAD_LOCAL,       1),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.CALL_FUNCTION,    1),
                Instruction.of(OpCode.LOAD_LOCAL,       2),
                Instruction.of(OpCode.LOAD_CONSTANT,    2),
                Instruction.of(OpCode.CALL_FUNCTION,    1),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE),
        };

        MonaObject[] main_consts = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(1),
                MonaNumber.newInteger(2),
        };

        String[] main_localvars = new String[] {"addgen", "adder1", "adder2"};

        ////////////////// addgen ////////////////////
        Instruction[] addgen_instr = new Instruction[] {
                Instruction.of(OpCode.MAKE_FUNCTION,2),
                Instruction.of(OpCode.STORE_LOCAL,  1),
                Instruction.of(OpCode.LOAD_LOCAL,   1),
                Instruction.of(OpCode.RETURN_VALUE),
        };

        MonaObject[] addgen_consts = new MonaObject[] {
                MonaNull.NIL,
        };

        String[] addgen_localvars = new String[] {"x", "adder"};

        ////////////////// adder ////////////////////
        Instruction[] adder_instr = new Instruction[] {
                Instruction.of(OpCode.LOAD_GLOBAL,  0),
                Instruction.of(OpCode.LOAD_LOCAL,   0),
                Instruction.of(OpCode.BINARY_ADD),
                Instruction.of(OpCode.RETURN_VALUE),
        };

        MonaObject[] adder_consts = new MonaObject[] {
                MonaNull.NIL,
        };

        String[] adder_lcoalvars = new String[] {"y"};

        String[] adder_globalvars = new String[] {"x"};

        Integer[] adder_globalpos = new Integer[] {0};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(main_instr).info("__main__", 0).vars(main_consts, main_localvars),
                BasicBlock.build(addgen_instr).info("__addgen__", main_instr.length).vars(addgen_consts, addgen_localvars),
                BasicBlock.build(adder_instr).info("__addgen_adder__", main_instr.length + adder_instr.length)
                                             .vars(adder_consts, adder_lcoalvars, adder_globalvars, adder_globalpos),
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        MonaObject res = vm.run(TestUtils.inputOf());

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 6);
    }


}
