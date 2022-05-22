package org.lds56.mona.core.script;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.TestUtils;
import org.lds56.mona.engine.MonaEngine;

/**
 * @Author: Rui Chen
 * @Date: 18 May 2022
 * @Description: This is description.
 */
public class ArithmeticTest {

    private MonaEngine engine = new MonaEngine();

    @Test
    public void testAdd() {

        String expr1 = "1+2+3+4+5+6+7+8+9+10";
        String expr2 = "a+b-c+(d-1)";

        Assertions.assertEquals(55, engine.execute(expr1));
        Assertions.assertEquals(3, engine.execute(expr2, TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4)));
    }

    @Test
    public void testMulti() {

        String expr1 = "1*3/2*4";
        String expr2 = "a+b/c*(d-1)";

        Assertions.assertEquals(4, engine.execute(expr1));
        Assertions.assertEquals(1, engine.execute(expr2, TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4)));
    }
}
