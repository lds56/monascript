package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.TestUtils;

import java.util.Arrays;

/**
 * @Author: Rui Chen
 * @Date: 02 Jun 2022
 * @Description: This is description.
 */
public class LambdaTest extends ScriptTest {

    @Test
    public void testLambdaCall() {

        String s = "let mult2 = x -> 2*x; return mult2(10);";
        Object result = engine.execute(s);

        Assertions.assertEquals(result, 20);
    }

    @Test
    public void testLambdaCall2() {

        String s = "let l = (a,b) -> a+b; return l(1,2);";
        Object result = engine.execute(s);

        Assertions.assertEquals(result, 3);
    }

    @Test
    public void testLambdaCall3() {

        String s = "let func = (l, a) -> {" +
                   "    return l[a];" +
                   "}; " +
                   "return func(arr, idx);";
        Object result = engine.execute(s, TestUtils.inputOf("arr", Arrays.asList(0,1,2,3,4,5), "idx", 3));

        Assertions.assertEquals(result, 3);
    }
}
