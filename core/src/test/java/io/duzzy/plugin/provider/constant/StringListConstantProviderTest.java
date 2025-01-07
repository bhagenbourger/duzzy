package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StringListConstantProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-list-provider-full.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringListConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo("one");
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-list-provider.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringListConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo("constant");
  }

  @Test
  void computeValueIsIdempotent() {
    final String value = new StringListConstantProvider(List.of("one", "two", "three"))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("one");
  }

  @Test
  void computeValueIsInConstants() {
    final String value = new StringListConstantProvider(List.of("one", "two", "three"))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("three");
  }

  @Test
  void corruptedValueIsIdempotent() {
    final String value = new StringListConstantProvider(List.of("one", "two", "three"))
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
  }
}
