package io.duzzy.core.provider.constant;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.util.List;

public abstract class WeightedListConstantProvider<T> implements Provider<T> {

  private final List<WeightedItem<T>> values;
  private final Integer totalWeight;

  public WeightedListConstantProvider(
      List<WeightedItem<T>> values
  ) {
    this.values = values;
    this.totalWeight = values
        .stream()
        .map(WeightedItem::weight)
        .mapToInt(Integer::intValue)
        .sum();
  }

  @Override
  public T value(FieldContext fieldContext) {
    int rand = fieldContext.random().nextInt(0, totalWeight);
    int i = 0;
    while (i < values.size() - 1 && rand > 0) {
      rand -= values.get(i).weight();
      i++;
    }
    return values.get(i).value();
  }

  public List<WeightedItem<T>> getValues() {
    return values;
  }

  public Integer getTotalWeight() {
    return totalWeight;
  }
}
