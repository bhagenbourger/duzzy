package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class BigDecimalIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/big-decimal-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigDecimalIncrementProvider.class);
    assertThat((BigDecimal) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(new BigDecimal("13.0"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/big-decimal-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigDecimalIncrementProvider.class);
    assertThat((BigDecimal) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final BigDecimal value =
        new BigDecimalIncrementProvider(new BigDecimal("10.5"), new BigDecimal("2.5"))
            .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigDecimal("13.0"));
  }

  @Test
  void computeValueWithRightRange() {
    final BigDecimal value =
        new BigDecimalIncrementProvider(new BigDecimal("10.5"), new BigDecimal("2.5"))
            .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(new BigDecimal("23.0"));
  }
}
