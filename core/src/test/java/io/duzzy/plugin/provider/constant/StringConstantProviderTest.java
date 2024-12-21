package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.SEEDED_FIVE_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class StringConstantProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/constant/string-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(StringConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo("constant");
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/constant/string-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(StringConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo("constant");
  }

  @Test
  void computeValueIsIdempotent() {
    final String value =
        new StringConstantProvider("myValue").value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo("myValue");
  }

  @Test
  void computeValueIsConstant() {
    final String value =
        new StringConstantProvider("myValue").value(SEEDED_FIVE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo("myValue");
  }

  @Test
  void corruptedValueIsIdempotent() {
    final String value =
        new StringConstantProvider("myValue").corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
  }
}
