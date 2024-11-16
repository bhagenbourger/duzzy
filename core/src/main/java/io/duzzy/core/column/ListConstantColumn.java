package io.duzzy.core.column;

import java.util.List;
import java.util.Objects;

public abstract class ListConstantColumn<T> extends Column<T> {

    private final List<T> values;

    public ListConstantColumn(String name, ColumnType columnType, Float nullRate, List<T> values) {
        super(name, columnType, nullRate);
        this.values = values;
    }

    @Override
    protected T computeValue(ColumnContext columnContext) {
        return values.get(columnContext.random().nextInt(0, values.size()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), values);
    }

    public List<T> getValues() {
        return values;
    }
}
