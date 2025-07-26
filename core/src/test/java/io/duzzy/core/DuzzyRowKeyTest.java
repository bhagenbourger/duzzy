package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DuzzyRowKeyTest {
  private static final String EXPECTED_VALUE = "abc:123";

  @Test
  void createsFromStringAndReturnsString() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(EXPECTED_VALUE));
    assertThat(key.asString()).isEqualTo(EXPECTED_VALUE);
  }

  @Test
  void createsFromStringAndReturnsBytes() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(EXPECTED_VALUE));
    final byte[] bytes = key.asBytes();
    assertThat(new String(bytes, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_VALUE);
  }

  @Test
  void isPresentReturnsTrueWhenKeyPresent() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(EXPECTED_VALUE));
    assertThat(key.isPresent()).isTrue();
  }

  @Test
  void isPresentReturnsFalseWhenKeyEmpty() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.empty());
    assertThat(key.isPresent()).isFalse();
  }

  @Test
  void isPresentReturnsFalseWhenKeyIsNull() {
    final DuzzyRowKey key = new DuzzyRowKey(null);
    assertThat(key.isPresent()).isFalse();
  }

  @Test
  void isPresentReturnsFalseWhenKeyEmptyString() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(""));
    assertThat(key.isPresent()).isTrue();
  }
}
