package io.duzzy.plugin.provider.increment;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class BigIntegerIncrementProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/big-integer-increment-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigIntegerIncrementProvider.class);
    assertThat((BigInteger) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(new BigInteger("12"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/increment/big-integer-increment-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigIntegerIncrementProvider.class);
    assertThat((BigInteger) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isNotNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final BigInteger value =
        new BigIntegerIncrementProvider(new BigInteger("10"), new BigInteger("2"))
            .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigInteger("12"));
  }

  @Test
  void computeValueWithRightRange() {
    final BigInteger value =
        new BigIntegerIncrementProvider(new BigInteger("10"), new BigInteger("2"))
            .value(new FieldContext(null, false, null, 5L, 5L));
    assertThat(value).isEqualTo(new BigInteger("20"));
  }
}
