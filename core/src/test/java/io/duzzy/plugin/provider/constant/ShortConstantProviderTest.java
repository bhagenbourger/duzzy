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

public class ShortConstantProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/short-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(ShortConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo((short) 1);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/short-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(ShortConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Short value = new ShortConstantProvider((short) 3).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo((short) 3);
  }

  @Test
  void computeValueIsConstant() {
    final Short value = new ShortConstantProvider((short) 3).value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo((short) 3);
  }
}
