package io.duzzy.core.column;

import io.duzzy.core.Plugin;

import java.util.Objects;

public abstract class Column<T> implements Plugin {

    protected static final Float DEFAULT_NULL_RATE = 0f;

    private final String name;
    private final ColumnType columnType;
    private final Float nullRate;

    public Column(String name, ColumnType columnType, Float nullRate) {
        assert name != null : "Column name can't be null in column " + getIdentifier();
        assert columnType != null : "Column type can't be null in column " + getIdentifier();
        assert nullRate == null || (nullRate >= 0 && nullRate <= 1) : "Column nullRate must be between 0 and 1 in colum " + name;
        this.name = name;
        this.columnType = columnType;
        this.nullRate = nullRate == null ? DEFAULT_NULL_RATE : nullRate;
    }

    public T value(ColumnContext columnContext) {
        if (columnContext.random().nextFloat(0, 1) < nullRate) {
            return null;
        } else {
            return computeValue(columnContext);
        }
    }

    protected abstract T computeValue(ColumnContext columnContext);

    public String getName() {
        return name;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public Float getNullRate() {
        return nullRate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, columnType, nullRate);
    }
}


