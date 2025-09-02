package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class LocalDateTimeConstantProviderTest {

  private static final String VAL = "2023-01-01T12:00:00";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/localdatetime-constant-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateTimeConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isEqualTo(LocalDateTime.parse("2023-01-01T12:00:00"));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/constant/localdatetime-constant-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(LocalDateTimeConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isNull();
  }

  @Test
  void computeValueIsIdempotent() {
    final LocalDateTime value = new LocalDateTimeConstantProvider(LocalDateTime.parse(VAL))
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDateTime.parse(VAL));
  }

  @Test
  void computeValueIsConstant() {
    final LocalDateTime value = new LocalDateTimeConstantProvider(LocalDateTime.parse(VAL))
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(LocalDateTime.parse(VAL));
  }
}
