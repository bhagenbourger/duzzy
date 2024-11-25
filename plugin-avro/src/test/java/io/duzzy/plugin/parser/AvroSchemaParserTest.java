package io.duzzy.plugin.parser;

import io.duzzy.core.column.Column;
import io.duzzy.core.schema.DuzzySchema;
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
        final DuzzySchema duzzySchema = new AvroSchemaParser().parse(avroSchema, null);

        assertThat(duzzySchema.columns()).hasSize(16);
        assertThat(duzzySchema.columns().getFirst())
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("string_field", "io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
        assertThat(duzzySchema.columns().get(1))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("int_field", "io.duzzy.plugin.provider.random.IntegerRandomProvider");
        assertThat(duzzySchema.columns().get(2))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("long_field", "io.duzzy.plugin.provider.random.LongRandomProvider");
        assertThat(duzzySchema.columns().get(3))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("float_field", "io.duzzy.plugin.provider.random.FloatRandomProvider");
        assertThat(duzzySchema.columns().get(4))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("double_field", "io.duzzy.plugin.provider.random.DoubleRandomProvider");
        assertThat(duzzySchema.columns().get(5))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("boolean_field", "io.duzzy.plugin.provider.random.BooleanRandomProvider");
        assertThat(duzzySchema.columns().get(6))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_str", "io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
        assertThat(duzzySchema.columns().get(7))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_int", "io.duzzy.plugin.provider.random.IntegerRandomProvider");
        assertThat(duzzySchema.columns().get(8))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_long", "io.duzzy.plugin.provider.random.LongRandomProvider");
        assertThat(duzzySchema.columns().get(9))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_float", "io.duzzy.plugin.provider.random.FloatRandomProvider");
        assertThat(duzzySchema.columns().get(10))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_double", "io.duzzy.plugin.provider.random.DoubleRandomProvider");
        assertThat(duzzySchema.columns().get(11))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("nullable_boolean", "io.duzzy.plugin.provider.random.BooleanRandomProvider");
        assertThat(duzzySchema.columns().get(12))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("uuid_field", "io.duzzy.plugin.provider.random.UUIDRandomProvider");
        assertThat(duzzySchema.columns().get(13))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("date_field", "io.duzzy.plugin.provider.random.LocalDateRandomProvider");
        assertThat(duzzySchema.columns().get(14))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("timestamp_millis_field", "io.duzzy.plugin.provider.random.InstantRandomProvider");
        assertThat(duzzySchema.columns().get(15))
                .extracting(Column::name, c -> c.providers().getFirst().getIdentifier())
                .containsExactly("timestamp_micros_field", "io.duzzy.plugin.provider.random.InstantRandomProvider");
        assertThat(duzzySchema.sink().getIdentifier()).isEqualTo(DuzzySchema.DEFAULT.sink().getIdentifier());
        assertThat(duzzySchema.rows()).isEqualTo(DuzzySchema.DEFAULT.rows());
        assertThat(duzzySchema.seed()).isNotEqualTo(DuzzySchema.DEFAULT.seed());
    }

    @Test
    void notSupportedFields() {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/all-unsupported-fields.avsc");
        assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Field name 'decimal_field' - avro logical type 'decimal' is not supported");
    }

    @Test
    void emptyAvroSchemaToDuzzy() throws IOException {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/empty-fields.avsc");
        final DuzzySchema duzzySchema = new AvroSchemaParser().parse(avroSchema, null);

        assertThat(duzzySchema.columns()).isEqualTo(DuzzySchema.DEFAULT.columns());
        assertThat(duzzySchema.sink().getIdentifier()).isEqualTo(DuzzySchema.DEFAULT.sink().getIdentifier());
        assertThat(duzzySchema.rows()).isEqualTo(DuzzySchema.DEFAULT.rows());
        assertThat(duzzySchema.seed()).isNotEqualTo(DuzzySchema.DEFAULT.seed());
    }

    @Test
    void invalidAvroSchema() {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/invalid.avsc");
        assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null)).isInstanceOf(AvroTypeException.class);
    }
}
