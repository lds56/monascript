package org.lds56.mona.core.script;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.lds56.mona.core.codegen.InterpreterByteCodeGen;
import org.lds56.mona.core.interpreter.ByteCode;
import org.lds56.mona.core.interpreter.ByteCodeBlock;
import org.lds56.mona.core.syntax.antlr.MonaLexer;
import org.lds56.mona.core.syntax.antlr.MonaParser;
import org.lds56.mona.core.syntax.ast.ASTParserVisitor;
import org.lds56.mona.engine.EngineOption;
import org.lds56.mona.engine.MonaEngine;
import org.lds56.mona.script.BytecodeScript;
import org.lds56.mona.script.MonaScript;


/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class ScriptComposer {

    public static MonaScript create(String code) {

        CodePointCharStream inputStream = CharStreams.fromString(code);
        MonaLexer lexer = new MonaLexer(inputStream);

        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MonaParser parser = new MonaParser(tokenStream);

        switch (MonaEngine.getEngineMode().getChoice(EngineOption.SCRIPT_EXECUTION_MODE)) {
            case BYTECODE_SCRIPT:
                return byyyyyte(parser);
            case EVAL_EXPRESSION:
                return null;
            default:
                return null;
        }

    }

    private static MonaScript byyyyyte(MonaParser parser) {

        InterpreterByteCodeGen codegen = new InterpreterByteCodeGen();
        ASTParserVisitor<ByteCodeBlock> vistor = new ASTParserVisitor<>(codegen);

        ByteCodeBlock mainBlock = vistor.visit(parser.script());
        ByteCode bc = codegen.generate(mainBlock);

        return new BytecodeScript(bc);
    }
}
