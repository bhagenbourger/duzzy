package io.duzzy.plugin.parser;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.field.Field;
import io.duzzy.core.schema.DuzzySchema;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.apache.avro.AvroTypeException;
import org.junit.jupiter.api.Test;

public class AvroSchemaParserTest {

  @Test
  void avroSchemaToDuzzySchema() throws IOException {
    final File avroSchema = getFromResources(getClass(), "avro-schemas/all-supported-fields.avsc");
    final DuzzySchema duzzySchema = new AvroSchemaParser().parse(avroSchema, null);

    assertThat(duzzySchema.fields()).hasSize(16);
    assertThat(duzzySchema.fields().getFirst())
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("string_field",
            "io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
    assertThat(duzzySchema.fields().get(1))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("int_field", "io.duzzy.plugin.provider.random.IntegerRandomProvider");
    assertThat(duzzySchema.fields().get(2))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("long_field", "io.duzzy.plugin.provider.random.LongRandomProvider");
    assertThat(duzzySchema.fields().get(3))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("float_field", "io.duzzy.plugin.provider.random.FloatRandomProvider");
    assertThat(duzzySchema.fields().get(4))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("double_field", "io.duzzy.plugin.provider.random.DoubleRandomProvider");
    assertThat(duzzySchema.fields().get(5))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("boolean_field", "io.duzzy.plugin.provider.random.BooleanRandomProvider");
    assertThat(duzzySchema.fields().get(6))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("nullable_str",
            "io.duzzy.plugin.provider.random.AlphanumericRandomProvider");
    assertThat(duzzySchema.fields().get(7))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("nullable_int", "io.duzzy.plugin.provider.random.IntegerRandomProvider");
    assertThat(duzzySchema.fields().get(8))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("nullable_long", "io.duzzy.plugin.provider.random.LongRandomProvider");
    assertThat(duzzySchema.fields().get(9))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("nullable_float", "io.duzzy.plugin.provider.random.FloatRandomProvider");
    assertThat(duzzySchema.fields().get(10))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("nullable_double", "io.duzzy.plugin.provider.random.DoubleRandomProvider");
    assertThat(duzzySchema.fields().get(11))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("nullable_boolean",
            "io.duzzy.plugin.provider.random.BooleanRandomProvider");
    assertThat(duzzySchema.fields().get(12))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("uuid_field", "io.duzzy.plugin.provider.random.UuidRandomProvider");
    assertThat(duzzySchema.fields().get(13))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("date_field", "io.duzzy.plugin.provider.random.LocalDateRandomProvider");
    assertThat(duzzySchema.fields().get(14))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("timestamp_millis_field",
            "io.duzzy.plugin.provider.random.InstantRandomProvider");
    assertThat(duzzySchema.fields().get(15))
        .extracting(Field::name, c -> c.providers().getFirst().getIdentifier())
        .containsExactly("timestamp_micros_field",
            "io.duzzy.plugin.provider.random.InstantRandomProvider");
  }

  @Test
  void notSupportedFields() {
    final File avroSchema =
        getFromResources(getClass(), "avro-schemas/all-unsupported-fields.avsc");
    assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Field name 'decimal_field' - avro type 'decimal' is not supported");
  }

  @Test
  void emptyAvroSchemaToDuzzyContext() throws IOException {
    final File avroSchema = getFromResources(getClass(), "avro-schemas/empty-fields.avsc");
    final DuzzySchema duzzySchema = new AvroSchemaParser().parse(avroSchema, null);

    assertThat(duzzySchema.fields()).isEqualTo(new DuzzySchema(Optional.empty(), null).fields());
  }

  @Test
  void invalidAvroSchema() {
    final File avroSchema = getFromResources(getClass(), "avro-schemas/invalid.avsc");
    assertThatThrownBy(() -> new AvroSchemaParser().parse(avroSchema, null)).isInstanceOf(
        AvroTypeException.class);
  }
}
