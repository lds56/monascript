package org.lds56.mona.core.runtime;

public interface Computable<T> {

    default T add(T other) {
        throw new UnsupportedOperationException("Unsupported `add` operation");
    }

    default T sub(T other) {
        throw new UnsupportedOperationException("Unsupported `sub` operation");
    }

    default T mult(T other) {
        throw new UnsupportedOperationException("Unsupported `mult` operation");
    }

    default T div(T other) {
        throw new UnsupportedOperationException("Unsupported `div` operation");
    }

    default T mod(T other) {
        throw new UnsupportedOperationException("Unsupported `mod` operation");
    }

    default T neg(T other) {
        throw new UnsupportedOperationException("Unsupported `neg` operation");
    }

}
