package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.RANDOM_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class LongRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/random/long-random-column-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(LongRandomProvider.class);
    assertThat((Long) provider.value(SEEDED_ONE_COLUMN_CONTEXT.get()))
        .isGreaterThanOrEqualTo(0L)
        .isLessThanOrEqualTo(150L);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile = getFromResources(getClass(), "provider/random/long-random-column.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(LongRandomProvider.class);
    assertThat((Long) provider.value(SEEDED_ONE_COLUMN_CONTEXT.get()))
        .isGreaterThanOrEqualTo(Long.MIN_VALUE)
        .isLessThanOrEqualTo(Long.MAX_VALUE);
  }

  @Test
  void computeValueIsIdempotent() {
    final Long value = new LongRandomProvider()
        .value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(-4964420948893066024L);
  }

  @Test
  void computeValueWithRightRange() {
    final Long value = new LongRandomProvider(5L, 6L)
        .value(RANDOM_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(5L);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Long value = new LongRandomProvider(3L, 4L)
        .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(-4964420948893066024L);
  }
}
