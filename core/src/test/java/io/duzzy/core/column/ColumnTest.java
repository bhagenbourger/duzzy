package io.duzzy.core.column;

import io.duzzy.core.provider.ColumnType;
import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ColumnTest {

    @Test
    void columnNameCantBeNull() {
        assertThatThrownBy(() -> new Column(null, null, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column name can't be null or empty");
    }

    @Test
    void columnNameCantBeEmpty() {
        assertThatThrownBy(() -> new Column("", null, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column name can't be null or empty");
    }

    @Test
    void columnTypeCantBeNull() {
        assertThatThrownBy(() -> new Column("ok", null, null, null))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column type can't be null");
    }

    @Test
    void columnProvidersCantBeNull() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, null, null))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Providers can't be null or empty");
    }

    @Test
    void columnProvidersCantBeEmpty() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, null, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Providers can't be null or empty");
    }

    @Test
    void nullRateDefaultValueShouldBe0() {
        final Column column = new Column(
                "ok",
                ColumnType.BOOLEAN,
                null,
                List.of(new BooleanConstantProvider(Boolean.TRUE)));

        assertThat(column.nullRate()).isEqualTo(0f);
    }

    @Test
    void nullRateMustBeBetween0and1() {
        assertThatThrownBy(() -> new Column("ok", ColumnType.DECIMAL, 2f, List.of()))
                .isInstanceOf(java.lang.AssertionError.class)
                .hasMessage("Column nullRate must be between 0 and 1");
    }
}
