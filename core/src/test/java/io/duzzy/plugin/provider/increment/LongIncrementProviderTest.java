package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.RANDOM_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class LongIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/increment/long-increment-column-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(LongIncrementProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(110L);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/increment/long-increment-column.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(LongIncrementProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(1L);
  }

  @Test
  void computeValueWithRightStartingValue() {
    final Long value = new LongIncrementProvider(100L, 10L)
        .value(RANDOM_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(100L);
  }

  @Test
  void computeValueWithRightFirstStepValue() {
    final Long value = new LongIncrementProvider(100L, 10L)
        .value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(110L);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Long value = new LongIncrementProvider(3L, 1L)
        .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(-4964420948893066024L);
  }
}
