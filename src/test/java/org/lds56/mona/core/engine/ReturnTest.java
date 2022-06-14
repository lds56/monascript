package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Author: Rui Chen
 * @Date: 14 Jun 2022
 * @Description: This is description.
 */
public class ReturnTest extends ScriptTest {

    @Test
    public void testReturn() {
        String expr = "fn f(a) { return a+1; } return f(10);";
        Object result = engine.execute(expr);
        Assertions.assertEquals(result, 11);
    }

    @Test
    public void testMutipleReturn() {
        String expr = "fn swap(a, b) { return b, a; } let x, y = swap(10, 5); return x-y;";
        Object result = engine.execute(expr);
        Assertions.assertEquals(result, -5);
    }

    @Test
    public void testNoReturn() {
        String expr = "fn ff(x) { return; } return ff(10);";
        Object result = engine.execute(expr);
        Assertions.assertNull(result);
    }
}
