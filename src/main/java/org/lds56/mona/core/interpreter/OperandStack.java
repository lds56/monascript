package org.lds56.mona.core.interpreter;

import java.util.EmptyStackException;
import java.util.Vector;

@Deprecated
public class OperandStack<T> extends Vector<T> {

    OperandStack() {}

    public void push(T obj) {
        add(obj);
    }

    public T top() {
        int len = size();

        if (len == 0)
            throw new EmptyStackException();

        return elementAt(len - 1);
    }

    public T pop() {

        T obj = top();
        removeElementAt(size() - 1);

        return obj;
    }

    public void top(T obj) {
        int len = size();

        if (len == 0)
            throw new EmptyStackException();

        setElementAt(obj, len - 1);
    }
}
