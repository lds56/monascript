package org.lds56.mona.core.benchmark;

import org.lds56.mona.engine.MonaEngine;
import org.lds56.mona.script.MonaScript;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class CompilationProfilerTest {

    public static void main(String[] args)  {

        MonaEngine engine = new MonaEngine();

        String expr = "(a+b*c/d-1>3)? a+b : a-b";

        MonaScript result = engine.compile(expr);

    }

}
