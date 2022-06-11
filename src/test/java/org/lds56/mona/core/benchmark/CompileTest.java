package org.lds56.mona.core.benchmark;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import org.lds56.mona.engine.MonaEngine;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Rui Chen
 * @Date: 08 June 2022
 * @Description: This is description.
 */

@State(Scope.Thread)
@Threads(2)
@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class CompileTest {

    String expr3;

    AviatorEvaluatorInstance aviatorEngine = AviatorEvaluator.getInstance();

    MonaEngine monaEngine = new MonaEngine();

    @Setup(Level.Trial)
    public void prepare() {

        expr3 = "i * pi + (d * b - 199) / (1 - d * pi) - (2 + 100 - i / pi) % 99 >=13";

    }

    @Benchmark
    public void testCompileAviatorExpr3() {
        aviatorEngine.compile(expr3);
    }

    @Benchmark
    public void testCompileMonaExpr3() {
        monaEngine.compile(expr3);
    }


    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder().include(CompileTest.class.getSimpleName())
                                              .warmupIterations(3)
                                              .measurementIterations(5)
                                              .forks(1)
                                              .build();

        new Runner(options).run();
    }
}
