package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Type;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DuzzyRowTest {

  @Test
  void sizeOfValuesReturnsZeroForEmptyRow() {
    final DuzzyRow row = new DuzzyRow(List.of());
    assertThat(row.sizeOfValues()).isEqualTo(0);
  }

  @Test
  void sizeOfValuesReturnsCorrectCountForMultipleValues() {
    final List<DuzzyCell> cells = List.of(
        new DuzzyCell("id", Type.INTEGER, 1),
        new DuzzyCell("name", Type.STRING, "test"),
        new DuzzyCell("active", Type.BOOLEAN, true)
    );
    final DuzzyRow row = new DuzzyRow(cells);
    assertThat(row.sizeOfValues()).isEqualTo(9);
  }
}
