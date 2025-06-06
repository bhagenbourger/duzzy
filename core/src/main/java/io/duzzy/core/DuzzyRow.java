package io.duzzy.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record DuzzyRow(
    Optional<Object> rowKey,
    List<DuzzyCell> cells
) {

  public DuzzyRow(List<DuzzyCell> cells) {
    this(Optional.empty(), cells);
  }

  public List<Object> toValues() {
    return cells.stream().map(DuzzyCell::value).toList();
  }

  public Map<String, Object> toMap() {
    return cells.stream().collect(
        LinkedHashMap::new,
        (m, v) -> m.put(v.name(), v.value()),
        LinkedHashMap::putAll
    );
  }
}
