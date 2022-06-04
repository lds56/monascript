package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @Author: Rui Chen
 * @Date: 04 Jun 2022
 * @Description: This is description.
 */
public class AssignmentTest extends ScriptTest {

    @Test
    public void  testSelfAdd() {

        String expr = "let a=1; a+=a; return a;";
        Object result = engine.execute(expr);

        Assertions.assertEquals(result, 2);
    }
}
