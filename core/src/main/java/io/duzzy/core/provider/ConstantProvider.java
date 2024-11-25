package io.duzzy.core.provider;

import io.duzzy.core.column.ColumnContext;

public abstract class ConstantProvider<T> implements Provider<T> {

    private final T value;

    public ConstantProvider(T value) {
        this.value = value;
    }

    @Override
    public T value(ColumnContext columnContext) {
        return value;
    }

    public T getValue() {
        return value;
    }
}
