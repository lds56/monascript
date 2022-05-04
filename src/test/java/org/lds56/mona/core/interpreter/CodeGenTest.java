package org.lds56.mona.core.interpreter;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.codegen.InterpreterByteCodeGen;
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

        CodePointCharStream inputStream = CharStreams.fromString(expr);
        MonaLexer lexer = new MonaLexer(inputStream);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MonaParser parser = new MonaParser(tokenStream);

        InterpreterByteCodeGen codegen = new InterpreterByteCodeGen();
        ASTParserVisitor<ByteCodeBlock> vistor = new ASTParserVisitor<>(codegen);

        ByteCodeBlock mainBlock = vistor.visit(parser.script());
        ByteCode bc = codegen.generate(mainBlock);

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
    public void testAddGen() {

        String expr = "1+1";
        Object result = interpret(expr);

        Assertions.assertEquals(result, 2);
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
}
