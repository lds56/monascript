package org.lds56.mona.utils;

import java.util.ArrayList;
import java.util.List;

public class PointerStack<T> implements EasyStack<T> {

    public static class Pointer<T> {
        private T item;
        private Pointer<T> prev;
        private Pointer<T> next;
        public Pointer(T item, Pointer<T> prev, Pointer<T> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
        public Pointer<T> forward(T item) {
            if (this.next == null) {
                this.next = new Pointer<>(item, this, null);
            } else {
                this.next.item = item;
            }
            return this.next;
        }
        public Pointer<T> backward() {
            // TODO: if (this.prev == null)
            return this.prev;
        }
//        public T item() {
//            return this.item;
//        }
    }

    public final Pointer<T> DUMMY_POINTER = new Pointer<>(null, null, null);

    private Pointer<T> topPointer;

    public PointerStack() {
        topPointer = DUMMY_POINTER;
    }

    @Override
    public void push(T obj) {
        topPointer = topPointer.forward(obj);
    }

    @Override
    public T top() {
        return topPointer.item;
    }

    @Override
    public T pop() {
        topPointer = topPointer.backward();
        return topPointer.next.item;
    }

    @Override
    public void top(T obj) {
        topPointer.item = obj;
    }

    @Override
    public T peek() {
        return topPointer.item;
    }

    @Override
    public boolean isEmpty() {
        return topPointer == DUMMY_POINTER;
    }

    @Override
    public void clear() {
        topPointer = DUMMY_POINTER;
    }

    @Override
    public List<T> toList() {
        Pointer<T> ptr = DUMMY_POINTER;
        List<T> l = new ArrayList<>();
        while (ptr != topPointer) {
            ptr = ptr.next;
            l.add(ptr.item);
        }
        return l;
    }
}
