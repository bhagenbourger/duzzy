package io.duzzy.plugin.provider.random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.provider.Provider;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class InstantRandomProviderTest {

  private static final String START = "2020-05-26T23:59:59.999999999Z";
  private static final String END = "2020-05-27T23:59:59.999999999Z";


  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/instant-random-provider-full.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(InstantRandomProvider.class);
    assertThat((Instant) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(Instant.parse(START))
        .isBefore(Instant.parse(END));
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile =
        getFromResources(getClass(), "provider/random/instant-random-provider.yaml");
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(InstantRandomProvider.class);
    assertThat((Instant) provider.value(SEEDED_ONE_FIELD_CONTEXT.get()))
        .isAfterOrEqualTo(Instant.MIN)
        .isBeforeOrEqualTo(Instant.MAX);
  }

  @Test
  void errorWhenMaxIsBeforeMin() {
    assertThatThrownBy(() -> new InstantRandomProvider(END, START))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Min instant must be before max instant");
  }

  @Test
  void computeValueIsIdempotent() {
    final Instant value = new InstantRandomProvider()
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("6836-01-05T14:44:02.796Z");
  }

  @Test
  void computeValueWithRightRange() {
    final Instant value = new InstantRandomProvider(START, END)
        .value(RANDOM_FIELD_CONTEXT.get());
    assertThat(value)
        .isAfterOrEqualTo(START)
        .isBeforeOrEqualTo(END);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final Instant value = new InstantRandomProvider(START, END)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("-157314268-09-17T15:02:13.976Z");
  }
}
