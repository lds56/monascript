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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ByteCodeTest {


    @Test
    public void testExpr() {

        // b? 1+1 : 3
        Instruction[] ins = new Instruction[] {
                Instruction.of(OpCode.LOAD_LOCAL, 0),
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
                BasicBlock.build(0, ins, consts, localNames).withName("__main__")
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
                Instruction.of(OpCode.LOAD_LOCAL, 1),
                Instruction.of(OpCode.CALL_FUNCTION, 1),
                Instruction.of(OpCode.RETURN_VALUE)
                // [Nil, 1, 'f', 2]
        };
        MonaObject[] consts_main = new MonaObject[] {
                MonaNull.NIL,
                MonaString.valueOf("f"),
                MonaString.valueOf("y"),
                // MonaNumber.newInteger(1),        // bb index
        };

        String[] localNames_main = new String[] {"f", "y"};

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
                BasicBlock.build(0, ins_main, consts_main, localNames_main).withName("__f__"),
                BasicBlock.build(ins_main.length, ins_f, consts_f, localNames_f).withName("__main__")
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
                Instruction.of(OpCode.LOAD_GLOBAL,      2),
                Instruction.of(OpCode.LOAD_CONSTANT,    1),
                Instruction.of(OpCode.INPLACE_ADD),
                Instruction.of(OpCode.STORE_GLOBAL,     2),
                Instruction.of(OpCode.LOAD_LOCAL,       0),
                Instruction.of(OpCode.LOAD_GLOBAL,      2),
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
                BasicBlock.build(0, ins_main, consts_main, localNames_main).withName("__main__"),
                BasicBlock.build(ins_main.length, ins_f, consts_f, localNames_f).withName("__f1__")
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
                Instruction.of(OpCode.LOAD_LOCAL, 1),
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

        String[] localNames = new String[] {"x", "y"};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(0, ins, consts, localNames).withName("__main__")
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
                Instruction.of(OpCode.STORE_LOCAL, 1),
                Instruction.of(OpCode.LOAD_LOCAL, 0),
                Instruction.of(OpCode.GET_ITERATOR),
                Instruction.of(OpCode.NEXT_ITERATOR),
                Instruction.of(OpCode.BRANCH_FALSE, 20),
                Instruction.of(OpCode.STORE_LOCAL, 2),
                Instruction.of(OpCode.LOAD_LOCAL, 2),
                Instruction.of(OpCode.LOAD_CONSTANT, 2),
                Instruction.of(OpCode.BINARY_MODULO),
                Instruction.of(OpCode.LOAD_CONSTANT, 3),
                Instruction.of(OpCode.BRANCH_EQUAL, 4),
                Instruction.of(OpCode.LOAD_LOCAL, 2),
                Instruction.of(OpCode.LOAD_CONSTANT, 4),
                Instruction.of(OpCode.BRANCH_GREATER_THAN, 20),
                Instruction.of(OpCode.LOAD_LOCAL, 1),
                Instruction.of(OpCode.LOAD_LOCAL, 2),
                Instruction.of(OpCode.INPLACE_ADD),
                Instruction.of(OpCode.STORE_LOCAL, 1),
                Instruction.of(OpCode.JUMP_LOCAL, 4),
                Instruction.of(OpCode.LOAD_LOCAL, 1),
                Instruction.of(OpCode.RETURN_VALUE),
                };

        MonaObject[] consts = new MonaObject[] {
                MonaNull.NIL,
                MonaNumber.newInteger(0),
                MonaNumber.newInteger(2),
                MonaNumber.newInteger(1),
                MonaNumber.newInteger(10),
                };

        String[] localNames = new String[] {"l", "ans", "x"};

        ByteCode code = ByteCode.load(new BasicBlock[]{
                BasicBlock.build(0, ins, consts, localNames).withName("__main__")
        });

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(code);
        List<Integer> l =  Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).collect(Collectors.toList());
        MonaObject res = vm.run(TestUtils.inputOf("l", MonaObject.wrap(l)));

        System.out.println(res.getValue());
        Assertions.assertEquals(res.getValue(), 30);
    }
}
