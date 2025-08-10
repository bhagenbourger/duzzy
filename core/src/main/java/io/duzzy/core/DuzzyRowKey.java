package io.duzzy.core;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record DuzzyRowKey(
    Optional<Object> key
) {

  private static final Logger logger = LoggerFactory.getLogger(DuzzyRowKey.class);

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
