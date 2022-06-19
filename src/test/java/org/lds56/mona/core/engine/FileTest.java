package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.ScriptCase;

/**
 * @Author: Rui Chen
 * @Date: 12 May 2022
 * @Description: This is description.
 */
public class FileTest extends CaseTest {

    @ScriptCase(
            script = "let s, err = sys.fread('/Users/lds56/Workspace/fun/monascript/src/test/resources/test'); " +
                     "return s;",
            expected = "test"
    )
    public void case1() {}

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
