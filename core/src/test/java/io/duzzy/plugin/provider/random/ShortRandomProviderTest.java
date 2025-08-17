package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class ShortRandomProviderTest {

  private static final Short MIN_VALUE = 10;
  private static final Short MAX_VALUE = 100;

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/short-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(ShortRandomProvider.class);
    assertThat((Short) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(MIN_VALUE)
        .isLessThan(MAX_VALUE);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/short-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(ShortRandomProvider.class);
    assertThat((Short) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(Short.MIN_VALUE)
        .isLessThan(Short.MAX_VALUE);
  }

  @Test
  void computeValueIsIdempotent() {
    final Short value = new ShortRandomProvider(null, null).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo((short) 18502);
  }

  @Test
  void computeValueWithRightRange() {
    final Short value = new ShortRandomProvider(MIN_VALUE, MAX_VALUE)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).isGreaterThanOrEqualTo(MIN_VALUE).isLessThan(MAX_VALUE);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Short value = new ShortRandomProvider(MIN_VALUE, MAX_VALUE)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo((short) 18502);
  }
}
