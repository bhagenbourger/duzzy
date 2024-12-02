package io.duzzy.core.column;

import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import io.duzzy.plugin.provider.constant.StringConstantProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.duzzy.test.TestUtility.PROVIDERS_COLUMN_CONTEXT;
import static io.duzzy.test.TestUtility.RANDOM_COLUMN_CONTEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ColumnTest {

    public static void checkSometimesNull(Column column, int expected) {
        int cpt = 0;
        for (int i = 0; i < 1000; i++) {
            if (column.value(RANDOM_COLUMN_CONTEXT.get()) == null) {
                cpt++;
            }
        }
        assertThat(Math.round(cpt / 100f)).isEqualTo(expected);
    }

    @Test
    void columnNameCantBeNull() {
        assertThatThrownBy(() -> new Column(null, null, null, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column name can't be null or empty");
    }

    @Test
    void columnNameCantBeEmpty() {
        assertThatThrownBy(() -> new Column("", null, null, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column name can't be null or empty");
    }

    @Test
    void columnTypeCantBeNull() {
        assertThatThrownBy(() -> new Column("ok", null, null, null, null))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column type can't be null");
    }

    @Test
    void columnProvidersCantBeNull() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, null, null, null))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Providers can't be null or empty");
    }

    @Test
    void columnProvidersCantBeEmpty() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, null, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Providers can't be null or empty");
    }

    @Test
    void nullRateDefaultValueShouldBe0() {
        final Column column = new Column(
                "ok",
                ColumnType.BOOLEAN,
                null,
                null,
                List.of(new BooleanConstantProvider(Boolean.TRUE)));

        assertThat(column.nullRate()).isEqualTo(0f);
    }

    @Test
    void nullRateMustBeBetween0and1() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, 2f, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column nullRate must be between 0 and 1");
    }

    @Test
    void corruptedRateDefaultValueShouldBe0() {
        final Column column = new Column(
                "ok",
                ColumnType.BOOLEAN,
                null,
                null,
                List.of(new BooleanConstantProvider(Boolean.TRUE)));

        assertThat(column.corruptedRate()).isEqualTo(0f);
    }

    @Test
    void corruptedRateMustBeBetween0and1() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, null, 2f, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column corruptedRate must be between 0 and 1");
    }

    @Test
    void corrupted() {
        final Object value = new Column(
                "ok",
                ColumnType.BOOLEAN,
                null,
                1f,
                List.of(new BooleanConstantProvider(Boolean.TRUE))
        ).value(PROVIDERS_COLUMN_CONTEXT(false).get());

        assertThat(value).isInstanceOf(Long.class);
    }

    @Test
    void corruptedWithSchema() {
        final Object value = new Column(
                "ok",
                ColumnType.STRING,
                null,
                1f,
                List.of(new StringConstantProvider("const"))
        ).value(PROVIDERS_COLUMN_CONTEXT(true).get());

        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isNotEqualTo("const");
    }

    @Test
    void alwaysNull() {
        final Column column = new Column(
                "test",
                ColumnType.STRING,
                1f,
                0f,
                List.of(new AlphanumericRandomProvider())
        );
        checkSometimesNull(column, 10);
    }

    @Test
    void neverNull() {
        final Column column = new Column(
                "test",
                ColumnType.STRING,
                0f,
                0f,
                List.of(new AlphanumericRandomProvider())
        );
        checkSometimesNull(column, 0);
    }

    @Test
    void sometimesNull() {
        final Column column = new Column(
                "test",
                ColumnType.STRING,
                0.5f,
                0f,
                List.of(new AlphanumericRandomProvider())
        );
        checkSometimesNull(column, 5);
    }
}
