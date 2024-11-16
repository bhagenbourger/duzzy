package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzySchema;
import io.duzzy.core.Plugin;
import io.duzzy.core.column.Column;
import io.duzzy.core.config.DuzzyConfig;
import org.apache.avro.AvroTypeException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AvroSchemaParserTest {

    private static final DuzzySchema DEFAULT_DUMMY_SCHEMA = new DuzzySchema(null, null, null, null);

    @Test
    void avroSchemaToDuzzySchema() throws IOException {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/all-supported-fields.avsc");
        final DuzzySchema duzzySchema = new AvroSchemaParser().parse(avroSchema, null);

        assertThat(duzzySchema.columns()).hasSize(16);
        assertThat(duzzySchema.columns().getFirst())
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("string_field", "io.duzzy.plugin.column.random.AlphanumericRandomColumn");
        assertThat(duzzySchema.columns().get(1))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("int_field", "io.duzzy.plugin.column.random.IntegerRandomColumn");
        assertThat(duzzySchema.columns().get(2))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("long_field", "io.duzzy.plugin.column.random.LongRandomColumn");
        assertThat(duzzySchema.columns().get(3))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("float_field", "io.duzzy.plugin.column.random.FloatRandomColumn");
        assertThat(duzzySchema.columns().get(4))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("double_field", "io.duzzy.plugin.column.random.DoubleRandomColumn");
        assertThat(duzzySchema.columns().get(5))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("boolean_field", "io.duzzy.plugin.column.random.BooleanRandomColumn");
        assertThat(duzzySchema.columns().get(6))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("nullable_str", "io.duzzy.plugin.column.random.AlphanumericRandomColumn");
        assertThat(duzzySchema.columns().get(7))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("nullable_int", "io.duzzy.plugin.column.random.IntegerRandomColumn");
        assertThat(duzzySchema.columns().get(8))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("nullable_long", "io.duzzy.plugin.column.random.LongRandomColumn");
        assertThat(duzzySchema.columns().get(9))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("nullable_float", "io.duzzy.plugin.column.random.FloatRandomColumn");
        assertThat(duzzySchema.columns().get(10))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("nullable_double", "io.duzzy.plugin.column.random.DoubleRandomColumn");
        assertThat(duzzySchema.columns().get(11))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("nullable_boolean", "io.duzzy.plugin.column.random.BooleanRandomColumn");
        assertThat(duzzySchema.columns().get(12))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("uuid_field", "io.duzzy.plugin.column.random.UUIDRandomColumn");
        assertThat(duzzySchema.columns().get(13))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("date_field", "io.duzzy.plugin.column.random.LocalDateRandomColumn");
        assertThat(duzzySchema.columns().get(14))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("timestamp_millis_field", "io.duzzy.plugin.column.random.InstantRandomColumn");
        assertThat(duzzySchema.columns().get(15))
                .extracting(Column::getName, Plugin::getIdentifier)
                .containsExactly("timestamp_micros_field", "io.duzzy.plugin.column.random.InstantRandomColumn");
        assertThat(duzzySchema.sink().getIdentifier()).isEqualTo(DEFAULT_DUMMY_SCHEMA.sink().getIdentifier());
        assertThat(duzzySchema.rows()).isEqualTo(DEFAULT_DUMMY_SCHEMA.rows());
        assertThat(duzzySchema.seed()).isNotEqualTo(DEFAULT_DUMMY_SCHEMA.seed());
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

        assertThat(duzzySchema.columns()).isEqualTo(DEFAULT_DUMMY_SCHEMA.columns());
        assertThat(duzzySchema.sink().getIdentifier()).isEqualTo(DEFAULT_DUMMY_SCHEMA.sink().getIdentifier());
        assertThat(duzzySchema.rows()).isEqualTo(DEFAULT_DUMMY_SCHEMA.rows());
        assertThat(duzzySchema.seed()).isNotEqualTo(DEFAULT_DUMMY_SCHEMA.seed());
    }

    @Test
    void invalidAvroSchema() {
        final File avroSchema = getFromResources(getClass(), "avro-schemas/invalid.avsc");
        assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null)).isInstanceOf(AvroTypeException.class);
    }
}
