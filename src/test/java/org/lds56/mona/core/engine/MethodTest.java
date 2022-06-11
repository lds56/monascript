package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.TestUtils;

/**
 * @Author: Rui Chen
 * @Date: 28 May 2022
 * @Description: This is description.
 */
public class MethodTest extends ScriptTest {

    @Test
    public void testFoo() {

        Foo foo = new Foo(100);

        String expr = "foo.incBar() + foo.bar";
        Object result = engine.execute(expr, TestUtils.inputOf("foo", foo));

        Assertions.assertEquals(result, 202);
    }
}
