package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.TestUtils;
import org.lds56.mona.engine.MonaEngine;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class ArithmeticTest {

    @Test
    public void testAdd() {

        MonaEngine engine = new MonaEngine();

        String expr = "1+1";
        Object result = engine.execute(expr);

        Assertions.assertEquals(result, 2);
    }

    @Test
    public void testExpr() {

        MonaEngine engine = new MonaEngine();

        String expr = "(a+b*c/d-1>3)? a+b : a-b";
        Object result = engine.execute(expr, TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4));

        Assertions.assertEquals(result, -1);
    }
}
