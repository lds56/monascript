package org.lds56.mona.utils;

import java.util.Arrays;
import java.util.List;

public class ArrayStack<T> implements EasyStack<T> {

    private Object[] stack;

    private int stackTop;
    private int capacity;

    public ArrayStack() {
        capacity = 16;
        stack = new Object[capacity];
        stackTop = -1;
    }

    public ArrayStack(int cap) {
        capacity = cap;
        stack = new Object[cap];
        stackTop = -1;
    }

    @Override
    public void push(T obj) {
        if (stackTop + 1 >= capacity) {
            doubleCapacity();
        }
        stack[++stackTop] = obj;
    }

    @Override
    public T top() {
        return (T) stack[stackTop];
    }

    @Override
    public T pop() {
        return (T) stack[stackTop--];
    }

    @Override
    public void top(T obj) {
        stack[stackTop] = obj;
    }

    @Override
    public T peek() {
        return (T) stack[stackTop];
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
        Object[] arr = new Object[stackTop+1];
        System.arraycopy(stack, 0, arr, 0, stackTop+1);
        return (T[]) arr;
    }

    @Override
    public List<T> toList() {
        return Arrays.asList(toArray());
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Doubles the capacity of this deque.  Call only when full, i.e.,
     * when head and tail have wrapped around to become equal.
     */
    private void doubleCapacity() {
        int n = stack.length;
        int newCapacity = n << 1;
        Object[] a = new Object[newCapacity];
        System.arraycopy(stack, 0, a, 0, n);
        stack = a;
        capacity = a.length;
    }

//    /**
//     * Copies the elements from our element array into the specified array,
//     * in order (from first to last element in the deque).  It is assumed
//     * that the array is large enough to hold all elements in the deque.
//     *
//     * @return its argument
//     */
//    private <T> T[] copyElements(T[] a) {
//        if (head < tail) {
//            System.arraycopy(elements, head, a, 0, size());
//        } else if (head > tail) {
//            int headPortionLen = elements.length - head;
//            System.arraycopy(elements, head, a, 0, headPortionLen);
//            System.arraycopy(elements, 0, a, headPortionLen, tail);
//        }
//        return a;
//    }

}
