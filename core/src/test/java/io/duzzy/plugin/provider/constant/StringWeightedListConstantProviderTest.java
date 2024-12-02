package io.duzzy.plugin.provider.constant;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.provider.constant.WeightedItem;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestUtility.SEEDED_FIVE_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.SEEDED_ONE_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class StringWeightedListConstantProviderTest {

    private static final List<WeightedItem<String>> VALUES = List.of(
            new WeightedItem<>("one", 1),
            new WeightedItem<>("two", 2),
            new WeightedItem<>("three", 3)
    );

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "provider/constant/string-constant-weighted-list-provider-full.yaml"
        );
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(StringWeightedListConstantProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo("three");
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(
                getClass(),
                "provider/constant/string-constant-weighted-list-provider.yaml"
        );
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(StringWeightedListConstantProvider.class);
        assertThat(provider.value(SEEDED_ONE_COLUMN_CONTEXT.get())).isEqualTo("constant");
    }

    @Test
    void computeValueIsIdempotent() {
        final String value = new StringWeightedListConstantProvider(VALUES)
                .value(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo("three");
    }

    @Test
    void computeValueIsInConstants() {
        final String value = new StringWeightedListConstantProvider(VALUES)
                .value(SEEDED_FIVE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo("three");
    }

    @Test
    void corruptedValueIsIdempotent() {
        final String value = new StringWeightedListConstantProvider(VALUES)
                .corruptedValue(SEEDED_ONE_COLUMN_CONTEXT.get());
        assertThat(value).isEqualTo("Od`");
    }

    //TODO: test repartition
}
