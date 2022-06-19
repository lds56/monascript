package org.lds56.mona.core.interpreter;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.codegen.InterpreterByteCodeGen;
import org.lds56.mona.core.runtime.MonaGlobal;
import org.lds56.mona.core.syntax.antlr.MonaLexer;
import org.lds56.mona.core.syntax.antlr.MonaParser;
import org.lds56.mona.core.syntax.ast.ASTParserVisitor;
import org.lds56.mona.core.util.TestUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CodeGenTest {

    Object interpret(String expr) {
        return interpret(expr, new HashMap<>());
    }

    Object interpret(String expr, Map<String, Object> ctx) {

//        MonaFunction f1 = MonaFunction.newVarFunc(new NewListFunction());
//        MonaFunction f2 = MonaFunction.newVarFunc(new NewDictFunction());
//        MonaFunction f3 = MonaFunction.newVarFunc(new NewSetFunction());
//
//        Map<String, MonaObject> m = new HashMap<>();
//        m.put(f1.getName(), f1);
//        m.put(f2.getName(), f2);
//        m.put(f3.getName(), f3);

        MonaGlobal ggg = new MonaGlobal();

        CodePointCharStream inputStream = CharStreams.fromString(expr);
        MonaLexer lexer = new MonaLexer(inputStream);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MonaParser parser = new MonaParser(tokenStream);

        InterpreterByteCodeGen codegen = new InterpreterByteCodeGen();
        ASTParserVisitor<ByteCodeBlock> vistor = new ASTParserVisitor<>(codegen);

        ByteCodeBlock mainBlock = vistor.visit(parser.script());
        ByteCode bc = codegen.generate(mainBlock);
        bc.fillMainGlobals(ggg.getModule());

        // print
        for (BasicBlock bb : bc.getBasicBlocks()) {
            System.out.println("------------------");
            System.out.println(bb.toString());
        }
        System.out.println("==================");

        VirtualMach vm = VirtualMachFactory.createVM();
        vm.load(bc);
        return vm.run(ctx).getValue();

        // System.out.println("Interpret expr: " + expr + ", result: " + result);
        // return result;
    }

    @Test
    public void testAdd() {

        String expr = "1+1";
        Object result = interpret(expr);

        Assertions.assertEquals(result, 2);
    }

    @Test
    public void testExpr() {

        String expr = "(a+b*c/d-1>3)? a+b : a-b";
        Object result = interpret(expr, TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4));

        Assertions.assertEquals(result, -1);
    }

    @Test
    public void testWhile() {

        String expr = "let a=1; while a<10 { a=a+1; } return a;";
        // System.out.println(interpret(expr));
        Object result = interpret(expr);

        Assertions.assertEquals(result, 10);
    }


    @Test
    public void testIf() {

        String expr = "let a=1; if a % 2==1 { a=-1; } return a;";
        // System.out.println(interpret(expr));
        Object result = interpret(expr);

        Assertions.assertEquals(result, -1);
    }

    @Test
    public void testIfElse() {

        String expr = "let a=0; if a % 2==1 { a=-1; } else { a=1; } return a;";
        // System.out.println(interpret(expr));
        Object result = interpret(expr);

        Assertions.assertEquals(result, 1);
    }

    @Test
    public void testFor() {

        String expr = // "let a=[1,2,3,4,5]; " +
                      "let ans=0; " +
                      "for i in l { " +
                      "     ans = ans + i; " +
                      "} " +
                      "return ans;";
        // System.out.println(interpret(expr));
        Object result = interpret(expr, TestUtils.inputOf("l", Arrays.asList(1, 2, 3, 4, 5)));

        Assertions.assertEquals(result, 15);
    }

    @Test
    public void testFuncCall() {

        String expr =
                "let g = 10;" +
                "let f = fn(x) {" +
                "   return 2*x + g;" +
                "}; " +
                "return f(100);";
        // System.out.println(interpret(expr));
        Object result = interpret(expr, TestUtils.inputOf());

        Assertions.assertEquals(result, 210);
    }

    @Test
    public void testClosure() {

        String expr =
                "fn addgen(x) {\n" +
                "    fn adder(y) {\n" +
                "        return x+y;\n" +
                "    }\n" +
                "    return adder;\n" +
                "}\n" +
                "let adder1 = addgen(1);\n" +
                "let adder2 = addgen(2);\n" +
                "return adder1(1) + adder2(2);";

        // System.out.println(interpret(expr));
        Object result = interpret(expr, TestUtils.inputOf());

        Assertions.assertEquals(result, 6);
    }

    @Test
    public void testList() {

        String expr =
                "let l = [1,2,3];\n" +
                "let m = {1: \"1\", \"2\": 2};\n" +
                "return l[1] + m[\"2\"];";

        // System.out.println(interpret(expr));
        Object result = interpret(expr, TestUtils.inputOf());

        Assertions.assertEquals(result, 4);
    }
}
