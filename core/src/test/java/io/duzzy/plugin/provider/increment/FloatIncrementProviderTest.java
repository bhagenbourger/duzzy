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

public class FloatIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/increment/float-increment-column-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(FloatIncrementProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(100.5f);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/increment/float-increment-column.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(FloatIncrementProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(0.1f);
  }

  @Test
  void computeValueWithRightStartingValue() {
    final Float value = new FloatIncrementProvider(100f, 0.5f)
        .value(RANDOM_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(100f);
  }

  @Test
  void computeValueWithRightFirstStepValue() {
    final Float value = new FloatIncrementProvider(100f, 0.5f)
        .value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(100.5f);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Float value = new FloatIncrementProvider(3f, 0.1f)
        .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(2.4870493E38f);
  }
}
