package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class BooleanRandomProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/boolean-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BooleanRandomProvider.class);
    assertThat((Boolean) provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isTrue();
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/boolean-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BooleanRandomProvider.class);
    assertThat((Boolean) provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isTrue();
  }

  @Test
  void computeValueIsIdempotent() {
    final Boolean value = new BooleanRandomProvider()
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isTrue();
  }

  @Test
  void corruptedValueIsConstant() {
    final Boolean value = new BooleanRandomProvider()
        .corruptedValue(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isTrue();
  }
}
