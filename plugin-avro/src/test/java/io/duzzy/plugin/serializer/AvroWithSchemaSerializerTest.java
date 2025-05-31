package io.duzzy.plugin.serializer;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.tests.Data;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.junit.jupiter.api.Test;

public class AvroWithSchemaSerializerTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File serializerFile =
        getFromResources(getClass(), "serializer/avro-with-schema-serializer.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

    assertThat(serializer).isInstanceOf(AvroWithSchemaSerializer.class);
  }

  @Test
  void parsedFromYamlFull() throws IOException {
    final File serializerFile =
        getFromResources(getClass(), "serializer/avro-with-schema-serializer-full.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

    assertThat(serializer).isInstanceOf(AvroWithSchemaSerializer.class);
    final Schema schema = ((AvroWithSchemaSerializer) serializer).getSchema();
    assertThat(schema.getName()).isEqualTo("User");
    assertThat(schema.getNamespace()).isEqualTo("example.avro");
    assertThat(schema.getFields()).hasSize(16);
  }

  @Test
  void serializeWithDefaultValues() throws IOException {
    final String expectedSchema =
        "{\"type\":\"record\",\"name\":\"name\",\"namespace\":\"namespace\",\"fields\":["
            + "{\"name\":\"c1\",\"type\":\"int\"},"
            + "{\"name\":\"c2\",\"type\":\"string\"}"
            + "]}";
    try (final DataFileReader<GenericData.Record> records = computeRecords()) {
      assertThat(records.getSchema().toString()).isEqualTo(expectedSchema);

      GenericData.Record record = records.next();
      assertThat(record.get("c1")).isEqualTo(1);
      assertThat(record.get("c2").toString()).isEqualTo("one");

      record = records.next();
      assertThat(record.get("c1")).isEqualTo(2);
      assertThat(record.get("c2").toString()).isEqualTo("two");

      assertThat(records.hasNext()).isFalse();
    }
  }

  private static DataFileReader<GenericData.Record> computeRecords() throws IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final List<Field> fields = List.of(
        new Field(
            "c1",
            Type.INTEGER,
            null,
            null,
            List.of(new IntegerIncrementProvider(null, null))
        ),
        new Field(
            "c2",
            Type.STRING,
            null,
            null,
            List.of(new AlphanumericRandomProvider())
        )
    );

    final AvroWithSchemaSerializer avroWithSchemaSerializer =
        new AvroWithSchemaSerializer(null, null, null);
    avroWithSchemaSerializer.init(outputStream, new DuzzySchema(Optional.empty(), fields), 2L);
    avroWithSchemaSerializer.serialize(Data.getDataOne());
    avroWithSchemaSerializer.serialize(Data.getDataTwo());
    avroWithSchemaSerializer.close();

    return new DataFileReader<>(
        new SeekableByteArrayInput(outputStream.toByteArray()),
        new GenericDatumReader<>()
    );
  }

  //TODO: add test about writeUnit
  //TODO: add test about specified values
}
