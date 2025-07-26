package io.duzzy.core;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public record DuzzyRowKey(
    Optional<Object> key
) {
  public String asString() {
    return key.map(Object::toString).orElse(null);
  }

  public byte[] asBytes() {
    return key.map(DuzzyRowKey::serialize).orElse(null);
  }

  public boolean isPresent() {
    return key != null && key.isPresent();
  }

  private static byte[] serialize(Object obj) {
    return obj.toString().getBytes(StandardCharsets.UTF_8);
  }
}
