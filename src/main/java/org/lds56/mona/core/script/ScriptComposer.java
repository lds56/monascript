package org.lds56.mona.core.script;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.lds56.mona.core.codegen.InterpreterByteCodeGen;
import org.lds56.mona.core.interpreter.ByteCode;
import org.lds56.mona.core.interpreter.ByteCodeBlock;
import org.lds56.mona.core.runtime.functions.MonaFunction;
import org.lds56.mona.core.runtime.types.MonaObject;
import org.lds56.mona.core.syntax.antlr.MonaLexer;
import org.lds56.mona.core.syntax.antlr.MonaParser;
import org.lds56.mona.core.syntax.ast.ASTParserVisitor;
import org.lds56.mona.engine.EngineOption;
import org.lds56.mona.engine.MonaEngine;
import org.lds56.mona.library.java.coll.NewDictFunction;
import org.lds56.mona.library.java.coll.NewListFunction;
import org.lds56.mona.library.java.coll.NewRangeFunction;
import org.lds56.mona.library.java.coll.NewSetFunction;
import org.lds56.mona.script.BytecodeScript;
import org.lds56.mona.script.MonaScript;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class ScriptComposer {

    private static final Map<String, MonaObject> m;

    static {
        m = new HashMap<>();

        MonaFunction f1 = MonaFunction.newFunc(new NewListFunction());
        MonaFunction f2 = MonaFunction.newFunc(new NewDictFunction());
        MonaFunction f3 = MonaFunction.newFunc(new NewSetFunction());
        MonaFunction f4 = MonaFunction.newFunc(new NewRangeFunction());

        m.put(f1.getName(), f1);
        m.put(f2.getName(), f2);
        m.put(f3.getName(), f3);
        m.put(f4.getName(), f4);
    }

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
        bc.fillMainGlobals(m);

        return new BytecodeScript(bc);
    }

}
