package org.lds56.mona.core.benchmark;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import org.lds56.mona.core.util.TestUtils;
import org.lds56.mona.engine.MonaEngine;
import org.lds56.mona.script.MonaScript;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */

@State(Scope.Thread)
@Threads(4)
@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SimpleCaseTest {

    Map<String, Expression> aviatorExprDict = new HashMap<>();
    Map<String, MonaScript> monaScriptDict = new HashMap<>();

    Map<String, Object> ctx3 = TestUtils.inputOf("i", 1, "pi", 3.1415926, "d", 1.2, "b", 1000);

    @Setup(Level.Trial)
    public void prepare() {

        String expr3 = "i * pi + (d * b - 199) / (1 - d * pi) - (2 + 100 - i / pi) % 99 >=13";

        aviatorExprDict.put("expr3", AviatorEvaluator.compile(expr3));
        monaScriptDict.put("expr3", new MonaEngine().compile(expr3));

    }

    @Benchmark
    public void testAviatorExpr3() {
        aviatorExprDict.get("expr3").execute(ctx3);
    }

    @Benchmark
    public void testMonaExpr3() {
        monaScriptDict.get("expr3").execute(ctx3);
    }


    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder().include(SimpleCaseTest.class.getSimpleName())
                                              .warmupIterations(5)
                                              .measurementIterations(10)
                                              .forks(1)
                                              .build();

        new Runner(options).run();
    }
}
