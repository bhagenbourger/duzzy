package io.duzzy.cli;

import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.DuzzyResult;
import java.time.Duration;
import org.junit.jupiter.api.Test;

class DuzzyResultTxtFormatterTest {

  @Test
  void shouldFormatDuzzyResultCorrectly() {
    final DuzzyResult duzzyResult = new DuzzyResult(Duration.ofMillis(500), 3L, 1024L, 1234L);
    final DuzzyResultTxtFormatter formatter = new DuzzyResultTxtFormatter();

    final String result = formatter.format(duzzyResult);

    assertThat(result).isEqualTo(
        "Duzzy generated 3 rows in PT0.5S which represent 1024 bytes of data with seed 1234");
  }
}