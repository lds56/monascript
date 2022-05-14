package org.lds56.mona.utils;

import java.util.List;

public interface EasyStack<T> {

    void push(T obj);

    T top();

    T pop();

    void top(T obj);

    T peek();

    boolean isEmpty();

    void clear();

    List<T> toList();
}
