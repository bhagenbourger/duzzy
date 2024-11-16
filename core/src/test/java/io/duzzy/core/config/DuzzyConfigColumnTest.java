package io.duzzy.core.config;

import io.duzzy.core.column.Column;
import io.duzzy.plugin.column.random.AlphanumericRandomColumn;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DuzzyConfigColumnTest {

    private static final DuzzyConfigColumn duzzyConfigColumnExact =
            new DuzzyConfigColumn(
                    "name=phone",
                    "io.duzzy.plugin.column.random.AlphanumericRandomColumn",
                    Map.of()
            );
    private static final DuzzyConfigColumn duzzyConfigColumnStartsWith =
            new DuzzyConfigColumn(
                    "name=phone.*",
                    "io.duzzy.plugin.column.random.AlphanumericRandomColumn",
                    Map.of()
            );

    @Test
    void shouldMatchOnExactName() {
        assertThat(duzzyConfigColumnExact.querySelectorMatcher("name", "phone")).isTrue();
    }

    @Test
    void shouldNotMatchOnNotExactName() {
        assertThat(duzzyConfigColumnExact.querySelectorMatcher("name", "telephone")).isFalse();
    }

    @Test
    void shouldNotMatchOnNotSameKey() {
        assertThat(duzzyConfigColumnExact.querySelectorMatcher("type", "phone")).isFalse();
    }

    @Test
    void shouldMatchOnStartsWithName() {
        assertThat(duzzyConfigColumnStartsWith.querySelectorMatcher("name", "phone_number")).isTrue();
    }
}
