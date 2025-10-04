package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.provider.constant.FileConstantProvider;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StringFileConstantProviderTest {

  private static final String TWO = "two";

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-file-provider-full.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringFileConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(TWO);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-file-provider.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringFileConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(TWO);
  }

  @Test
  void computeValueIsIdempotent() throws IOException {
    final String value = new StringFileConstantProvider(
        "src/test/resources/data/example-comma.csv",
        FileConstantProvider.FileFormat.CSV,
        "c1",
        Map.of()
    ).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(TWO);
  }

  @Test
  void corruptedValueIsIdempotent() throws IOException {
    final String value = new StringFileConstantProvider(
        "src/test/resources/data/example-comma.csv",
        FileConstantProvider.FileFormat.CSV,
        "c1",
        Map.of()
    ).corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("Od`P=\u0011[y%.\u001F\u0004f?F\u001Dc$\u0011Pk^n;J"
        + "%_K\u001Dm\u001F]y\\B$WuD\"P\u00136HK\u001El\u0016J%Hzf$v,(\u000EGoX+\u001B7-"
        + "&=\u000B&4\u0010ys9*uLKAAi2\u0000NzD\u0011XT\u00039YX\u0011Jm\n"
        + "\u000BQ`s*:\u000F$*'\u001Bj==\u001A\\iR");
  }

  @Test
  void computeValueFromCsvSemiColumnSeparated() throws IOException {
    final String value = new StringFileConstantProvider(
        "src/test/resources/data/example-semicolumn.csv",
        FileConstantProvider.FileFormat.CSV,
        "c1",
        Map.of("column_separator", ";")
    ).value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(TWO);
  }
}
