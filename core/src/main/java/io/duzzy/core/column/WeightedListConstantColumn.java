package io.duzzy.core.column;

import java.util.List;
import java.util.Objects;

public abstract class WeightedListConstantColumn<T> extends Column<T> {

    private final List<WeightedItem<T>> values;
    private final Integer totalWeight;

    public WeightedListConstantColumn(
            String name,
            ColumnType columnType,
            Float nullRate,
            List<WeightedItem<T>> values
    ) {
        super(name, columnType, nullRate);
        this.values = values;
        this.totalWeight = values
                .stream()
                .map(WeightedItem::weight)
                .mapToInt(Integer::intValue)
                .sum();
    }

    @Override
    protected T computeValue(ColumnContext columnContext) {
        int rand = columnContext.random().nextInt(0, totalWeight);
        int i = 0;
        while (i < values.size() - 1 && rand > 0) {
            rand -= values.get(i).weight();
            i++;
        }
        return values.get(i).value();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), values, totalWeight);
    }

    public List<WeightedItem<T>> getValues() {
        return values;
    }

    public Integer getTotalWeight() {
        return totalWeight;
    }
}
