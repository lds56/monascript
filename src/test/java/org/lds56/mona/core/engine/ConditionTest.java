package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.ScriptCase;
import org.lds56.mona.core.util.TestUtils;

import java.util.Map;

/**
 * @Author: Rui Chen
 * @Date: 12 June 2022
 * @Description: This is description.
 */
public class ConditionTest extends CaseTest {

    @ScriptCase(script = "true? 1 : 2", expected = "1")
    public void case1() {}

    @ScriptCase(script = "if false { return -1; } else { return 1; }", expected = "1")
    public void case2() {}

    @ScriptCase(script = "if a>b { return a; } else { return b; }", expected = "10")
    public Map<String, Object> case3() {
        return TestUtils.inputOf("a", 1, "b", 10);
    }

    @Test
    void test() throws Exception {
        testCases();
    }

//
//    @Test
//    public void testAdd() {
//
//        String expr = "1+1";
//        Object result = engine.execute(expr);
//
//        Assertions.assertEquals(result, 2);
//    }
//
//    @Test
//    public void testExpr() {
//
//        String expr = "(a+b*c/d-1>3)? a+b : a-b";
//        Object result = engine.execute(expr, TestUtils.inputOf("a", 1, "b", 2, "c", 3, "d", 4));
//
//        Assertions.assertEquals(result, -1);
//    }
}
