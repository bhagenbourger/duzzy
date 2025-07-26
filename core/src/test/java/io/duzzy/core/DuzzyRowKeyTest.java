package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DuzzyRowKeyTest {
  private static final String EXPECTED_VALUE = "abc:123";

  @Test
  @DisplayName("Creates DuzzyRowKey from string and returns correct string representation")
  void createsFromStringAndReturnsString() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(EXPECTED_VALUE));
    assertThat(key.asString()).isEqualTo(EXPECTED_VALUE);
  }

  @Test
  @DisplayName("Creates DuzzyRowKey from string and returns correct byte array representation")
  void createsFromStringAndReturnsBytes() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(EXPECTED_VALUE));
    final byte[] bytes = key.asBytes();
    assertThat(new String(bytes, StandardCharsets.UTF_8)).isEqualTo(EXPECTED_VALUE);
  }

  @DisplayName("isPresent returns true when key is present")
  @Test
  void isPresentReturnsTrueWhenKeyPresent() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(EXPECTED_VALUE));
    assertThat(key.isPresent()).isTrue();
  }

  @DisplayName("isPresent returns false when key is empty")
  @Test
  void isPresentReturnsFalseWhenKeyEmpty() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.empty());
    assertThat(key.isPresent()).isFalse();
  }

  @DisplayName("isPresent returns false when key is null")
  @Test
  void isPresentReturnsFalseWhenKeyIsNull() {
    final DuzzyRowKey key = new DuzzyRowKey(null);
    assertThat(key.isPresent()).isFalse();
  }

  @DisplayName("isPresent returns true when key is empty string")
  @Test
  void isPresentReturnsFalseWhenKeyEmptyString() {
    final DuzzyRowKey key = new DuzzyRowKey(Optional.of(""));
    assertThat(key.isPresent()).isTrue();
  }
}
