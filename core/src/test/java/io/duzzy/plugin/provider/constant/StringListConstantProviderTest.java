package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StringListConstantProviderTest {

  private static final String ONE = "one";
  private static final String TWO = "two";
  private static final String THREE = "three";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-list-provider-full.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringListConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(ONE);
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
    final String value = new StringListConstantProvider(List.of(ONE, TWO, THREE))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(ONE);
  }

  @Test
  void computeValueIsInConstants() {
    final String value = new StringListConstantProvider(List.of(ONE, TWO, THREE))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(THREE);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final String value = new StringListConstantProvider(List.of(ONE, TWO, THREE))
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
  }
}
