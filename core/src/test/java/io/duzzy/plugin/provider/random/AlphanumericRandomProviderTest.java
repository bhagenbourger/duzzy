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

public class AlphanumericRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/alphanumeric-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(AlphanumericRandomProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get()).toString().length()).isBetween(6, 8);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/alphanumeric-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(AlphanumericRandomProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get()).toString()).hasSizeBetween(10, 15);
  }

  @Test
  void computeValueIsIdempotent() {
    final String value = new AlphanumericRandomProvider()
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("FoM4kOLyCysoR");
  }

  @Test
  void computeValueWithRightLength() {
    final String value = new AlphanumericRandomProvider(5, 5)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).hasSize(5);
  }

  @Test
  void corruptedValueIsIdempotentAndRespectMaxLength() {
    final String value = new AlphanumericRandomProvider(5, 15)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
    assertThat(value).hasSizeBetween(0, 15);
  }
}
