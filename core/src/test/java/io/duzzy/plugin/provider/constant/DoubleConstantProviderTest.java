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

public class DoubleConstantProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/constant/double-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(DoubleConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo(1d);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File columnFile =
        getFromResources(getClass(), "provider/constant/double-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

    assertThat(provider).isInstanceOf(DoubleConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final Double value = new DoubleConstantProvider(3d).value(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(3d);
  }

  @Test
  void computeValueIsConstant() {
    final Double value = new DoubleConstantProvider(3d).value(SEEDED_FIVE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(3d);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Double value =
        new DoubleConstantProvider(3d).corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
    assertThat(value).isEqualTo(1.3138947058478963E308);
  }
}
