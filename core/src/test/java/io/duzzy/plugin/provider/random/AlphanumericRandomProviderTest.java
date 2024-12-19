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

public class AlphanumericRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/random/alphanumeric-random-column-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(AlphanumericRandomProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get()).toString().length()).isBetween(6, 8);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/random/alphanumeric-random-column.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(AlphanumericRandomProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get()).toString()).hasSizeBetween(10, 15);
  }

  @Test
  void computeValueIsIdempotent() {
    final String value = new AlphanumericRandomProvider()
        .value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo("FoM4kOLyCysoR");
  }

  @Test
  void computeValueWithRightLength() {
    final String value = new AlphanumericRandomProvider(5, 5)
        .value(RANDOM_COLUMN_CONTEXT.get());
    assertThat(value).hasSize(5);
  }

  @Test
  void corruptedValueIsIdempotentAndRespectMaxLength() {
    final String value = new AlphanumericRandomProvider(5, 15)
        .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
    assertThat(value).hasSizeBetween(0, 15);
  }
}
