package org.lds56.mona.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.lds56.mona.utils.ArrayStack;

/**
 * @Author: Rui Chen
 * @Date: 17 May 2022
 * @Description: This is description.
 */
public class StackTest {

    @Test
    void testArrayStackDouble() {
        ArrayStack<Integer> stack = new ArrayStack<>(10);
        for (int i=0; i<15; i++) {
            stack.push(1);
        }
        Assertions.assertEquals(stack.getCapacity(), 20);
        for (int i=0; i<15; i++) {
            stack.push(1);
        }
        Assertions.assertEquals(stack.getCapacity(), 40);
    }
}
