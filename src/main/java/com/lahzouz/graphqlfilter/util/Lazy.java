package com.lahzouz.graphqlfilter.util;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a value that is evaluated when it is needed.
 *
 * @param <T> Type of the lazy value
 */
public class Lazy<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private T value;

    /**
     * Creates a lazy value that uses the provided supplier to get the value.
     *
     * @param supplier is called when the value needs to be evaluated.
     */
    public Lazy(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        this.supplier = supplier;
    }

    /**
     * Gets a result.
     *
     * @return a result
     */
    @Override
    public synchronized T get() {
        if (value == null) {
            value = supplier.get();
        }
        return value;
    }
}
