package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class StringConstantProviderTest {

  private static final String MY_VALUE = "myValue";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/string-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo("constant");
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/string-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo("constant");
  }

  @Test
  void computeValueIsIdempotent() {
    final String value =
        new StringConstantProvider(MY_VALUE).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(MY_VALUE);
  }

  @Test
  void computeValueIsConstant() {
    final String value =
        new StringConstantProvider(MY_VALUE).value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(MY_VALUE);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final String value =
        new StringConstantProvider(MY_VALUE).corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
  }
}
