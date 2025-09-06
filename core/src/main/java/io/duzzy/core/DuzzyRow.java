package io.duzzy.core;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record DuzzyRow(
    DuzzyRowKey rowKey,
    List<DuzzyCell> cells
) {

  public DuzzyRow(List<DuzzyCell> cells) {
    this(new DuzzyRowKey(Optional.empty()), cells);
  }

  public List<Object> valuesAsList() {
    return cells.stream().map(DuzzyCell::value).toList();
  }

  public Map<String, Object> valuesAsMap() {
    return cells.stream().collect(
        LinkedHashMap::new,
        (m, v) -> m.put(v.name(), v.value()),
        LinkedHashMap::putAll
    );
  }

  public Integer sizeOfValues() {
    return cells
        .stream()
        .map(c -> c.value().toString().getBytes(StandardCharsets.UTF_8).length)
        .reduce(0, Integer::sum);
  }
}
