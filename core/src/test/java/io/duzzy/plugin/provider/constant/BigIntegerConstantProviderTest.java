package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import org.junit.jupiter.api.Test;

public class BigIntegerConstantProviderTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/biginteger-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigIntegerConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(new BigInteger("1"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/biginteger-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(BigIntegerConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final BigInteger value = new BigIntegerConstantProvider(new BigInteger("3"))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigInteger("3"));
  }

  @Test
  void computeValueIsConstant() {
    final BigInteger value = new BigIntegerConstantProvider(new BigInteger("3"))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(new BigInteger("3"));
  }
}
