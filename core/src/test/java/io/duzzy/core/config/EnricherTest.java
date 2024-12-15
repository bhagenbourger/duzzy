package io.duzzy.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

public class EnricherTest {

  private static final Enricher ENRICHER_EXACT =
      new Enricher(
          "name=phone",
          "io.duzzy.plugin.column.random.AlphanumericRandomColumn",
          Map.of()
      );
  private static final Enricher ENRICHER_STARTS_WITH =
      new Enricher(
          "name=phone.*",
          "io.duzzy.plugin.column.random.AlphanumericRandomColumn",
          Map.of()
      );

  @Test
  void shouldMatchOnExactName() {
    assertThat(ENRICHER_EXACT.querySelectorMatcher("name", "phone")).isTrue();
  }

  @Test
  void shouldNotMatchOnNotExactName() {
    assertThat(ENRICHER_EXACT.querySelectorMatcher("name", "telephone")).isFalse();
  }

  @Test
  void shouldNotMatchOnNotSameKey() {
    assertThat(ENRICHER_EXACT.querySelectorMatcher("type", "phone")).isFalse();
  }

  @Test
  void shouldMatchOnStartsWithName() {
    assertThat(ENRICHER_STARTS_WITH.querySelectorMatcher("name", "phone_number")).isTrue();
  }
}
