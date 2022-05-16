package org.lds56.mona.script;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.lds56.mona.core.codegen.ExpressionEvalCodeGen;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.syntax.antlr.MonaLexer;
import org.lds56.mona.core.syntax.antlr.MonaParser;
import org.lds56.mona.core.syntax.ast.ASTParserVisitor;

import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 16 May 2022
 * @Description: This is description.
 */
public class MonaExpression {

    private final String expr;

    public MonaExpression(String expr) {
        this.expr = expr;
    }

    public Object evaluate() {
        return evaluate(null);
    }

    public Object evaluate(Map<String, Object> context) {

        CodePointCharStream inputStream = CharStreams.fromString(expr);
        MonaLexer lexer = new MonaLexer(inputStream);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MonaParser parser = new MonaParser(tokenStream);

        ExpressionEvalCodeGen codegen = new ExpressionEvalCodeGen();

        if (context != null) {
            codegen.withContext(context);
        }

        ASTParserVisitor<MonaObject> visitor = new ASTParserVisitor<>(codegen);

        return visitor.visit(parser.script()).getValue();
    }
}
