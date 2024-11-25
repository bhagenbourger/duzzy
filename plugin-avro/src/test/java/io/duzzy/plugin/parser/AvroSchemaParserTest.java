package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.column.Column;
import org.apache.avro.AvroTypeException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AvroSchemaParserTest {

    @Test
    void avroSchemaToDuzzySchema() throws IOException {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/all-supported-fields.avsc");
        final DuzzyContext duzzyContext = new AvroSchemaParser().parse(avroSchema, null);

        assertThat(duzzyContext.columns()).hasSize(16);
        assertThat(duzzyContext.columns().getFirst())
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("string_field", "io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
        assertThat(duzzyContext.columns().get(1))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("int_field", "io.duzzy.plugin.provider.random.IntegerRandomProvider");
        assertThat(duzzyContext.columns().get(2))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("long_field", "io.duzzy.plugin.provider.random.LongRandomProvider");
        assertThat(duzzyContext.columns().get(3))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("float_field", "io.duzzy.plugin.provider.random.FloatRandomProvider");
        assertThat(duzzyContext.columns().get(4))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("double_field", "io.duzzy.plugin.provider.random.DoubleRandomProvider");
        assertThat(duzzyContext.columns().get(5))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("boolean_field", "io.duzzy.plugin.provider.random.BooleanRandomProvider");
        assertThat(duzzyContext.columns().get(6))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_str", "io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
        assertThat(duzzyContext.columns().get(7))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_int", "io.duzzy.plugin.provider.random.IntegerRandomProvider");
        assertThat(duzzyContext.columns().get(8))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_long", "io.duzzy.plugin.provider.random.LongRandomProvider");
        assertThat(duzzyContext.columns().get(9))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_float", "io.duzzy.plugin.provider.random.FloatRandomProvider");
        assertThat(duzzyContext.columns().get(10))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_double", "io.duzzy.plugin.provider.random.DoubleRandomProvider");
        assertThat(duzzyContext.columns().get(11))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_boolean", "io.duzzy.plugin.provider.random.BooleanRandomProvider");
        assertThat(duzzyContext.columns().get(12))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("uuid_field", "io.duzzy.plugin.provider.random.UUIDRandomProvider");
        assertThat(duzzyContext.columns().get(13))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("date_field", "io.duzzy.plugin.provider.random.LocalDateRandomProvider");
        assertThat(duzzyContext.columns().get(14))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("timestamp_millis_field", "io.duzzy.plugin.provider.random.InstantRandomProvider");
        assertThat(duzzyContext.columns().get(15))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("timestamp_micros_field", "io.duzzy.plugin.provider.random.InstantRandomProvider");
        assertThat(duzzyContext.sink().getIdentifier()).isEqualTo(DuzzyContext.DEFAULT.sink().getIdentifier());
        assertThat(duzzyContext.rows()).isEqualTo(DuzzyContext.DEFAULT.rows());
        assertThat(duzzyContext.seed()).isNotEqualTo(DuzzyContext.DEFAULT.seed());
    }

    @Test
    void notSupportedFields() {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/all-unsupported-fields.avsc");
        assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Field name 'decimal_field' - avro logical type 'decimal' is not supported");
    }

    @Test
    void emptyAvroSchemaToDuzzyContext() throws IOException {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/empty-fields.avsc");
        final DuzzyContext duzzyContext = new AvroSchemaParser().parse(avroSchema, null);

        assertThat(duzzyContext.columns()).isEqualTo(DuzzyContext.DEFAULT.columns());
        assertThat(duzzyContext.sink().getIdentifier()).isEqualTo(DuzzyContext.DEFAULT.sink().getIdentifier());
        assertThat(duzzyContext.rows()).isEqualTo(DuzzyContext.DEFAULT.rows());
        assertThat(duzzyContext.seed()).isNotEqualTo(DuzzyContext.DEFAULT.seed());
    }

    @Test
    void invalidAvroSchema() {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/invalid.avsc");
        assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null)).isInstanceOf(AvroTypeException.class);
    }
}
