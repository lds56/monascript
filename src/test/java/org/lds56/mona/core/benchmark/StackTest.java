package org.lds56.mona.core.benchmark;

import org.lds56.mona.utils.ArrayStack;
import org.lds56.mona.utils.DequeStack;
import org.lds56.mona.utils.PointerStack;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Rui Chen
 * @Date: 16 May 2022
 * @Description: Stack benchmark.
 */

@State(Scope.Thread)
@Threads(2)
@BenchmarkMode({ Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StackTest {

    @Setup(Level.Trial)
    public void prepare() {

    }

    @Benchmark
    public void testArrayStack() {
        ArrayStack<Integer> stack = new ArrayStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.peek();
        stack.push(4);
        stack.peek();
        stack.top(5);
        stack.pop();
        stack.peek();
        stack.pop();
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }


    @Benchmark
    public void testPointerStack() {
        PointerStack<Integer> stack = new PointerStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.peek();
        stack.push(4);
        stack.peek();
        stack.top(5);
        stack.pop();
        stack.peek();
        stack.pop();
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }



    @Benchmark
    public void testDequeStack() {
        DequeStack<Integer> stack = new DequeStack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.peek();
        stack.push(4);
        stack.peek();
        stack.top(5);
        stack.pop();
        stack.peek();
        stack.pop();
        if (!stack.isEmpty()) {
            stack.pop();
        }
    }

    public static void main(String[] args) throws RunnerException {

        Options options = new OptionsBuilder().include(StackTest.class.getSimpleName())
                                              .warmupIterations(2)
                                              .measurementIterations(5)
                                              .forks(1)
                                              .build();

        new Runner(options).run();
    }
}
