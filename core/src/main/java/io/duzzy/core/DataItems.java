package io.duzzy.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public record DataItems(
    List<DataItem> items
) {

  public List<Object> toValues() {
    return items.stream().map(DataItem::value).toList();
  }

  public Map<String, Object> toMap() {
    return items.stream().collect(
        LinkedHashMap::new,
        (m, v) -> m.put(v.name(), v.value()),
        LinkedHashMap::putAll
    );
  }
}
