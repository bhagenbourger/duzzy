package io.duzzy.plugin.provider.constant;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.Utility.SEEDED_FIVE_FIELD_CONTEXT;
import static io.duzzy.test.Utility.SEEDED_ONE_FIELD_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.provider.constant.WeightedItem;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StringWeightedListConstantProviderTest {

  private static final String ONE = "one";
  private static final String TWO = "two";
  private static final String THREE = "three";

  private static final List<WeightedItem<String>> VALUES = List.of(
      new WeightedItem<>(ONE, 1),
      new WeightedItem<>(TWO, 2),
      new WeightedItem<>(THREE, 3)
  );

  @Test
  void parsedFromYaml() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-weighted-list-provider-full.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringWeightedListConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo(THREE);
  }

  @Test
  void parsedFromYamlHasDefaultValues() throws IOException {
    final File providerFile = getFromResources(
        getClass(),
        "provider/constant/string-constant-weighted-list-provider.yaml"
    );
    final Provider<?> provider = YAML_MAPPER.readValue(providerFile, Provider.class);

    assertThat(provider).isInstanceOf(StringWeightedListConstantProvider.class);
    assertThat(provider.value(SEEDED_ONE_FIELD_CONTEXT.get())).isEqualTo("constant");
  }

  @Test
  void computeValueIsIdempotent() {
    final String value = new StringWeightedListConstantProvider(VALUES)
        .value(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(THREE);
  }

  @Test
  void computeValueIsInConstants() {
    final String value = new StringWeightedListConstantProvider(VALUES)
        .value(SEEDED_FIVE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo(THREE);
  }

  @Test
  void corruptedValueIsIdempotent() {
    final String value = new StringWeightedListConstantProvider(VALUES)
        .corruptedValue(SEEDED_ONE_FIELD_CONTEXT.get());
    assertThat(value).isEqualTo("Od`");
  }

  //TODO: test repartition
}
