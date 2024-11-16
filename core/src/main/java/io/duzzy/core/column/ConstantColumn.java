package io.duzzy.core.column;

import java.util.Objects;

public abstract class ConstantColumn<T> extends Column<T> {

    private final T value;

    public ConstantColumn(String name, ColumnType columnType, Float nullRate, T value) {
        super(name, columnType, nullRate);
        this.value = value;
    }

    @Override
    protected T computeValue(ColumnContext columnContext) {
        return value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    public T getValue() {
        return value;
    }
}
