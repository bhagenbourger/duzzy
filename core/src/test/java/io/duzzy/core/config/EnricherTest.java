package io.duzzy.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  private static final Enricher WRONG_NULL = new Enricher(null, null, null);
  private static final Enricher WRONG_QUERY_SELECTOR =
      new Enricher(
          "null",
          null,
          null
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

  @Test
  void shouldCheckArgumentsIsEmptyWhenNoErrors() {
    assertThat(ENRICHER_EXACT.checkArguments()).isEmpty();
  }

  @Test
  void shouldCheckArgumentsWithErrorsWhenNull() {
    final Optional<List<String>> errors = WRONG_NULL.checkArguments();
    assertThat(errors).contains(
        List.of(
            "Query selector is null in Enricher",
            "Provider identifier is null in Enricher"
        )
    );
  }

  @Test
  void shouldCheckArgumentsWithErrorsWhenWrongQuerySelector() {
    final Optional<List<String>> errors = WRONG_QUERY_SELECTOR.checkArguments();
    assertThat(errors).contains(
        List.of(
            "Query selector null is invalid in Enricher. "
                + "Query selector must be formatted like that: key=value",
            "Provider identifier is null in Enricher"
        )
    );
  }
}
