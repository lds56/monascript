package org.lds56.mona.core.evaluator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.codegen.ExpressionEvalCodeGen;
import org.lds56.mona.core.codegen.InterpreterByteCodeGen;
import org.lds56.mona.core.interpreter.*;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.syntax.antlr.MonaLexer;
import org.lds56.mona.core.syntax.antlr.MonaParser;
import org.lds56.mona.core.syntax.ast.ASTParserVisitor;
import org.lds56.mona.core.util.TestUtils;

import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 04 May 2022
 * @Description: This is description.
 */
public class EvalCodeGenTest {

    Object evaluate(String expr, Map<String, Object> ctx) {

        CodePointCharStream inputStream = CharStreams.fromString(expr);
        MonaLexer lexer = new MonaLexer(inputStream);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MonaParser parser = new MonaParser(tokenStream);

        ExpressionEvalCodeGen codegen = new ExpressionEvalCodeGen();
        codegen.withContext(ctx);

        ASTParserVisitor<MonaObject> visitor = new ASTParserVisitor<>(codegen);

        return visitor.visit(parser.script()).getValue();

        // System.out.println("Interpret expr: " + expr + ", result: " + result);
        // return result;
    }

    @Test
    public void testAdd() {

        String s = "1+2+3+4";

        Assertions.assertEquals(10, evaluate(s, TestUtils.inputOf()));


        String s2 = "1+2+3+4-(a+b)";

        Assertions.assertEquals(0, evaluate(s2, TestUtils.inputOf("a", 5, "b", 5)));
    }


}
