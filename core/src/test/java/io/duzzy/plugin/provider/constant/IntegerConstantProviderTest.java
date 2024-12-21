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

public class IntegerConstantProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/constant/integer-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(IntegerConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(1);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/constant/integer-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(IntegerConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Integer value = new IntegerConstantProvider(3).value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(3);
  }

  @Test
  void computeValueIsConstant() {
    final Integer value = new IntegerConstantProvider(3).value(SEEDED_FIVE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(3);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Integer value =
        new IntegerConstantProvider(3).corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(-1155869325);
  }
}
