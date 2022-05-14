package org.lds56.mona.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ArrayStack<T> implements EasyStack<T> {

    private T[] stack;

    private int stackTop;

    ArrayStack(Class<T> clazz) {
        stack = (T[]) Array.newInstance(clazz, 10);
        stackTop = -1;
    }

    ArrayStack(Class<T> clazz, int capicity) {
        stack = (T[]) Array.newInstance(clazz, capicity);
        stackTop = -1;
    }

    @Override
    public void push(T obj) {
        stack[++stackTop] = obj;
    }

    @Override
    public T top() {
        return stack[stackTop];
    }

    @Override
    public T pop() {
        return stack[stackTop--];
    }

    @Override
    public void top(T obj) {
        stack[stackTop] = obj;
    }

    @Override
    public T peek() {
        return stack[stackTop];
    }

    @Override
    public boolean isEmpty() {
        return stackTop < 0;
    }

    @Override
    public void clear() {
        stackTop = -1;
    }

    public T[] toArray() {
        return stack;
    }


    @Override
    public List<T> toList() {
        return Arrays.asList(stack);
    }
}
