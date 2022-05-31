package org.lds56.mona.core.engine;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.core.util.TestUtils;
import org.lds56.mona.engine.MonaEngine;

import java.util.Arrays;

/**
 * @Author: Rui Chen
 * @Date: 30 May 2022
 * @Description: This is description.
 */
public class CollectionTest {

    MonaEngine engine = new MonaEngine();

    @Test
    public void testListSize() {

        String s = "l.size()";
        Object o = engine.execute(s, TestUtils.inputOf("l", Arrays.asList(1, 2, 3, 4, 5)));
        Assertions.assertEquals(o, 5);
    }

    // TODO: implement +=
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
}
