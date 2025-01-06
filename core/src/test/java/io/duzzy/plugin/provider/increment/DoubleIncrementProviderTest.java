package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DoubleIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/double-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(DoubleIncrementProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(100.5d);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/double-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(DoubleIncrementProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(0.1d);
  }

  @Test
  void computeValueWithRightStartingValue() {
    final Double value = new DoubleIncrementProvider(100d, 0.5d)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(100d);
  }

  @Test
  void computeValueWithRightFirstStepValue() {
    final Double value = new DoubleIncrementProvider(100d, 0.5d)
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(100.5d);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Double value = new DoubleIncrementProvider(3d, 0.1d)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(1.3138947058478963E308);
  }
}
