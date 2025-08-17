package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class LocalDateConstantProviderTest {

  private static final String VAL = "2023-01-01";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/localdate-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(LocalDate.parse("2023-01-01"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/localdate-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalDate value = new LocalDateConstantProvider(LocalDate.parse(VAL))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDate.parse(VAL));
  }

  @Test
  void computeValueIsConstant() {
    final LocalDate value = new LocalDateConstantProvider(LocalDate.parse(VAL))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDate.parse(VAL));
  }
}
