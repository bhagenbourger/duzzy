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
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class BigIntegerRandomProviderTest {

  private static final BigInteger MIN_VALUE = new BigInteger("10");
  private static final BigInteger MAX_VALUE = new BigInteger("20");

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/big-integer-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigIntegerRandomProvider.class);
    assertThat((BigInteger) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(MIN_VALUE)
        .isLessThan(MAX_VALUE);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/big-integer-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigIntegerRandomProvider.class);
    assertThat((BigInteger) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isGreaterThanOrEqualTo(BigInteger.valueOf(Long.MIN_VALUE))
        .isLessThan(BigInteger.valueOf(Long.MAX_VALUE));
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new BigIntegerRandomProvider(MAX_VALUE, MIN_VALUE))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min big integer must be before max big integer");
  }

  @Test
  void computeValueIsIdempotent() {
    final BigInteger value = new BigIntegerRandomProvider(null, null)
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigInteger("8346606878496438297"));
  }

  @Test
  void computeValueWithRightRange() {
    final BigInteger value = new BigIntegerRandomProvider(MIN_VALUE, MAX_VALUE)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value).isGreaterThanOrEqualTo(MIN_VALUE).isLessThan(MAX_VALUE);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final BigInteger value = new BigIntegerRandomProvider(MIN_VALUE, MAX_VALUE)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigInteger("661286326212192387375779478527203120403146615861"));
  }
}
