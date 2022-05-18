package org.lds56.mona.utils;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

public class DequeStack<T> implements EasyStack<T> {

    private ArrayDeque<T> deque;

    private int stackTop;

    public DequeStack() {
        deque = new ArrayDeque<T>();
        stackTop = -1;
    }

    public DequeStack(int capicity) {
        deque = new ArrayDeque<>(capicity);
        stackTop = -1;
    }

    @Override
    public void push(T obj) {
        deque.push(obj);
    }

    @Override
    public T top() {
        return deque.peekFirst();
    }

    @Override
    public T pop() {
        return deque.pop();
    }

    @Override
    public void top(T obj) {
        deque.pop();
        deque.push(obj);
    }

    @Override
    public T peek() {
        return deque.peekFirst();
    }

    @Override
    public boolean isEmpty() {
        return deque.isEmpty();
    }

    @Override
    public void clear() {
        stackTop = -1;
    }

    public T[] toArray() {
        return (T[]) deque.toArray();
    }

    @Override
    public List<T> toList() {
        return Arrays.asList(toArray());
    }
}
