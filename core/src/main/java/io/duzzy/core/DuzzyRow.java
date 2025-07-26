package io.duzzy.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

  public String keyAsString() {
    return rowKey.map(Object::toString).orElse(null);
  }

  public byte[] keyAsBytes() throws IOException {
    return rowKey.isEmpty() ? null : serialize(rowKey.get());
  }

  private static byte[] serialize(Object obj) throws IOException {
    try (final ByteArrayOutputStream bos = new ByteArrayOutputStream();
         final ObjectOutputStream oos = new ObjectOutputStream(bos)) {
      oos.writeObject(obj);
      return bos.toByteArray();
    }
  }
}
