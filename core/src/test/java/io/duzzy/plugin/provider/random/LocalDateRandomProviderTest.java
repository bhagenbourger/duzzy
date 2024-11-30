package io.duzzy.plugin.provider.random;

import io.duzzy.core.provider.Provider;
import io.duzzy.core.column.ColumnContext;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Random;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.test.TestHelper.DUMMY_COLUMN_CONTEXT;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LocalDateRandomProviderTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/local-date-random-column-full.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LocalDateRandomProvider.class);
        assertThat((LocalDate) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isAfterOrEqualTo(LocalDate.of(2020, 5, 26))
                .isBefore(LocalDate.of(2020, 5, 27));
    }

    @Test
    void parsedFromYamlHasDefaultValues() throws IOException {
        final File columnFile = getFromResources(getClass(), "provider/random/local-date-random-column.yaml");
        final Provider<?> provider = YAML_MAPPER.readValue(columnFile, Provider.class);

        assertThat(provider).isInstanceOf(LocalDateRandomProvider.class);
        assertThat((LocalDate) provider.value(DUMMY_COLUMN_CONTEXT.get()))
                .isAfterOrEqualTo(LocalDate.MIN)
                .isBeforeOrEqualTo(LocalDate.MAX);
    }

    @Test
    void errorWhenMaxIsBeforeMin() {
        assertThatThrownBy(() -> new LocalDateRandomProvider("2020-10-10", "2000-01-01"))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Min local date must be before max local date");
    }

    @Test
    void computeValueIsIdempotent() {
        final LocalDate value = new LocalDateRandomProvider(null, null)
                .value(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("2014-04-07");
    }

    @Test
    void computeValueWithRightRange() {
        final LocalDate value = new LocalDateRandomProvider("2020-05-30", "2021-05-30")
                .value(new ColumnContext(new Random(), 1L, 1L));
        assertThat(value)
                .isAfterOrEqualTo("2020-05-30")
                .isBeforeOrEqualTo("2021-05-30");
    }

    @Test
    void corruptedValueIsIdempotent() {
        final LocalDate value = new LocalDateRandomProvider("2020-05-30", "2021-05-30")
                .corruptedValue(new ColumnContext(new Random(1L), 1L, 1L));
        assertThat(value).isEqualTo("+486231866-06-06");
    }
}
