package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class BigDecimalRandomProviderTest {

  private static final BigDecimal MIN_VALUE = new BigDecimal("10.5");
  private static final BigDecimal MAX_VALUE = new BigDecimal("11.5");

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/big-decimal-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigDecimalRandomProvider.class);
    assertThat((BigDecimal) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(MIN_VALUE)
        .isLessThan(MAX_VALUE);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/big-decimal-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigDecimalRandomProvider.class);
    assertThat((BigDecimal) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(BigDecimal.valueOf(Double.MIN_VALUE))
        .isLessThan(BigDecimal.valueOf(Double.MAX_VALUE));
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new BigDecimalRandomProvider(MAX_VALUE, MIN_VALUE))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min big decimal must be before max big decimal");
  }

  @Test
  void computeValueIsIdempotent() {
    final BigDecimal value =
        new BigDecimalRandomProvider(null, null).value(SEEDED_ONE_FIELD_CONTEXT.get());
    final String z83 =
        "00000000000000000000000000000000000000000000000000000000000000000000000000000000000";
    assertThat(value).isEqualTo(new BigDecimal(
        "131389470584789642076572238473713"
            + z83 + z83 + z83
            + "000000000000000000000000000.0000000000000000000000000000000000000000000000000000000"
            + z83 + z83 + z83 + "0000000000000000000131869686555387459"));
  }

  @Test
  void computeValueWithRightRange() {
    final BigDecimal value = new BigDecimalRandomProvider(MIN_VALUE, MAX_VALUE)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).isGreaterThanOrEqualTo(MIN_VALUE).isLessThan(MAX_VALUE);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final BigDecimal value = new BigDecimalRandomProvider(MIN_VALUE, MAX_VALUE)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigDecimal("-4.964420948893066024E-1761283677"));
  }
}
