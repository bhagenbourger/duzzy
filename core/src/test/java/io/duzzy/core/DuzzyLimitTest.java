package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class DuzzyLimitTest {

  @Test
  void isReachedReturnsFalseWhenNoLimitsAndRowsBelowDefault() {
    final DuzzyLimit limit = new DuzzyLimit();
    assertThat(limit.isReached(9, 0, 0)).isFalse();
  }

  @Test
  void isReachedReturnsTrueWhenNoLimitsAndRowsMeetsDefault() {
    final DuzzyLimit limit = new DuzzyLimit();
    assertThat(limit.isReached(10, 0, 0)).isTrue();
  }

  @Test
  void isReachedReturnsTrueWhenRowsLimitReached() {
    final DuzzyLimit limit = new DuzzyLimit(5L, null, null);
    assertThat(limit.isReached(5, 0, 0)).isTrue();
  }

  @Test
  void isReachedReturnsFalseWhenRowsBelowRowsLimit() {
    final DuzzyLimit limit = new DuzzyLimit(5L, null, null);
    assertThat(limit.isReached(4, 0, 0)).isFalse();
  }

  @Test
  void isReachedReturnsTrueWhenSizeLimitReached() {
    final DuzzyLimit limit = new DuzzyLimit(null, 100L, null);
    assertThat(limit.isReached(0, 100, 0)).isTrue();
  }

  @Test
  void isReachedReturnsFalseWhenSizeBelowSizeLimit() {
    final DuzzyLimit limit = new DuzzyLimit(null, 100L, null);
    assertThat(limit.isReached(0, 99, 0)).isFalse();
  }

  @Test
  void isReachedReturnsTrueWhenDurationLimitReached() {
    final DuzzyLimit limit = new DuzzyLimit(null, null, 5L);
    assertThat(limit.isReached(0, 0, 5000)).isTrue();
  }

  @Test
  void isReachedReturnsFalseWhenDurationBelowDurationLimit() {
    final DuzzyLimit limit = new DuzzyLimit(null, null, 50L);
    assertThat(limit.isReached(0, 0, 49)).isFalse();
  }

  @Test
  void withRowsReturnsNewInstanceWithUpdatedRows() {
    final DuzzyLimit limit = new DuzzyLimit(1L, 10L, 20L);
    final DuzzyLimit updated = limit.withRows(5L);
    assertThat(updated.rows()).isEqualTo(5L);
    assertThat(updated.size()).isEqualTo(10L);
    assertThat(updated.duration()).isEqualTo(20L);
  }

  @Test
  void withSizeReturnsNewInstanceWithUpdatedSize() {
    final DuzzyLimit limit = new DuzzyLimit(5L, 1L, 20L);
    final DuzzyLimit updated = limit.withSize(10L);
    assertThat(updated.rows()).isEqualTo(5L);
    assertThat(updated.size()).isEqualTo(10L);
    assertThat(updated.duration()).isEqualTo(20L);
  }

  @Test
  void withDurationReturnsNewInstanceWithUpdatedDuration() {
    final DuzzyLimit limit = new DuzzyLimit(5L, 10L, 1L);
    final DuzzyLimit updated = limit.withDuration(20L);
    assertThat(updated.rows()).isEqualTo(5L);
    assertThat(updated.size()).isEqualTo(10L);
    assertThat(updated.duration()).isEqualTo(20L);
  }

  @Test
  void withRowsReturnsSameInstanceWhenRowsIsNull() {
    final DuzzyLimit limit = new DuzzyLimit(5L, 10L, 20L);
    final DuzzyLimit updated = limit.withRows(null);
    assertThat(updated).isSameAs(limit);
  }

  @Test
  void withSizeReturnsSameInstanceWhenSizeIsNull() {
    final DuzzyLimit limit = new DuzzyLimit(5L, 10L, 20L);
    final DuzzyLimit updated = limit.withSize(null);
    assertThat(updated).isSameAs(limit);
  }

  @Test
  void withDurationReturnsSameInstanceWhenDurationIsNull() {
    final DuzzyLimit limit = new DuzzyLimit(5L, 10L, 20L);
    final DuzzyLimit updated = limit.withDuration(null);
    assertThat(updated).isSameAs(limit);
  }
}