package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.TestUtils;

import java.util.Arrays;

/**
 * @Author: Rui Chen
 * @Date: 30 May 2022
 * @Description: This is description.
 */
public class CollectionTest extends ScriptTest {

    @Test
    public void testListSize() {

        String s = "l.size()";
        Object o = engine.execute(s, TestUtils.inputOf("l", Arrays.asList(1, 2, 3, 4, 5)));
        Assertions.assertEquals(o, 5);
    }

    @Test
    public void testSumList() {

        String s = "let sum=0; for x in l { sum = sum + x; } return sum;";
        Object o = engine.execute(s, TestUtils.inputOf("l", Arrays.asList(1, 2, 3, 4, 5)));
        Assertions.assertEquals(o, 15);
    }

    @Test
    public void testIndexList() {
        String s = "l[1]";
        Object o = engine.execute(s, TestUtils.inputOf("l", Arrays.asList(1, 2, 3, 4, 5)));
        Assertions.assertEquals(o, 2);
    }

    @Test
    public void testRangeFor() {
        String s = "let sum = 0;" +
                   "for x in 1..10 {" +
                   "    sum = sum + x;" +
                   "}" +
                   "return sum;";
        Object o = engine.execute(s);
        Assertions.assertEquals(o, 45);
    }

    @Test
    public void testClosedRangeFor() {
        String s = "let sum = 0;" +
                   "for x in 1...10 {" +
                   "    sum = sum + x;" +
                   "}" +
                   "return sum;";
        Object o = engine.execute(s);
        Assertions.assertEquals(o, 55);
    }


    @Test
    public void testUnpackList() {
        String s = "let x, y = l;" +
                   "return x+y;";
        Object o = engine.execute(s, TestUtils.inputOf("l", Arrays.asList(1,2)));
        Assertions.assertEquals(o, 3);
    }

    @Test
    public void testUnpackSet() {
        String s = "let x, y, z = [1, 2, 3];" +
                   "return x+y+z;";
        Object o = engine.execute(s);
        Assertions.assertEquals(o, 6);
    }

    @Test
    public void testUnpackForIn() {
        String s = "let l = [[1,1], [2,2], [3,3]];" +
                   "let ans = 0;" +
                   "for x,y in l {" +
                   "    ans += x + y;" +
                   "}" +
                   "return ans";
        Object o = engine.execute(s);
        Assertions.assertEquals(o, 12);
    }

    @Test
    public void testUnpackForInMap() {
        String s = "let m = {'a': 1, 'b': 2, 'c': 3};" +
                   "let ans = 0;" +
                   "for k,v in m {" +
                   "    ans += v;" +
                   "}" +
                   "return ans";
        Object o = engine.execute(s);
        Assertions.assertEquals(o, 6);
    }
}
