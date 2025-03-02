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

public class IntegerRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/integer-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(IntegerRandomProvider.class);
    assertThat((Integer) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(0)
        .isLessThanOrEqualTo(150);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/integer-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(IntegerRandomProvider.class);
    assertThat((Integer) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(Integer.MIN_VALUE)
        .isLessThanOrEqualTo(Integer.MAX_VALUE);
  }

  @Test
  void computeValueIsIdempotent() {
    final Integer value = new IntegerRandomProvider()
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(-1155869325);
  }

  @Test
  void computeValueWithRightRange() {
    final Integer value = new IntegerRandomProvider(5, 6)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(5);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Integer value = new IntegerRandomProvider(3, 10)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(-1155869325);
  }
}
