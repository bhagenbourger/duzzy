package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class DoubleRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/double-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(DoubleRandomProvider.class);
    assertThat((Double) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(50d)
        .isLessThanOrEqualTo(100d);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/double-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(DoubleRandomProvider.class);
    assertThat((Double) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(Double.MIN_VALUE)
        .isLessThanOrEqualTo(Double.MAX_VALUE);
  }

  @Test
  void computeValueIsIdempotent() {
    final Double value = new DoubleRandomProvider()
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(1.3138947058478963E308);
  }

  @Test
  void computeValueWithRightRange() {
    final Double value = new DoubleRandomProvider(5d, 6d)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).isGreaterThanOrEqualTo(5d);
    assertThat(value).isLessThan(6d);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Double value = new DoubleRandomProvider(3d, 4d)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(1.3138947058478963E308);
  }
}
