package io.duzzy.plugin.serializer;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.INTEGER_ONE;
import static io.duzzy.tests.Data.INTEGER_TWO;
import static io.duzzy.tests.Data.KEY_C1;
import static io.duzzy.tests.Data.KEY_C2;
import static io.duzzy.tests.Data.STRING_ONE;
import static io.duzzy.tests.Data.STRING_TWO;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AvroSchemalessSerializerTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File serializerFile =
        getFromResources(getClass(), "serializer/avro-schemaless-serializer.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

    assertThat(serializer).isInstanceOf(AvroSchemalessSerializer.class);
  }

  @Test
  void serializeWithDefaultValues() throws IOException {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final List<Field> fields = List.of(
        new Field(
            KEY_C1,
            Type.INTEGER,
            null,
            null,
            List.of(new IntegerIncrementProvider(null, null))
        ),
        new Field(
            KEY_C2,
            Type.STRING,
            null,
            null,
            List.of(new AlphanumericRandomProvider())
        )
    );

    final AvroSchemalessSerializer avroSchemalessSerializer =
        new AvroSchemalessSerializer(null, null, null);
    avroSchemalessSerializer.init(outputStream, new DuzzySchema(Optional.empty(), fields));
    avroSchemalessSerializer.serialize(getDataOne());
    avroSchemalessSerializer.serialize(getDataTwo());

    final DatumReader<GenericData.Record> reader = new GenericDatumReader<>();
    reader.setSchema(avroSchemalessSerializer.getSchema());
    final BinaryDecoder decoder = DecoderFactory
        .get()
        .binaryDecoder(new ByteArrayInputStream(outputStream.toByteArray()), null);
    GenericData.Record record = reader.read(null, decoder);
    assertThat(record.get(KEY_C1)).isEqualTo(INTEGER_ONE);
    assertThat(record.get(KEY_C2).toString()).isEqualTo(STRING_ONE);

    record = reader.read(null, decoder);
    assertThat(record.get(KEY_C1)).isEqualTo(INTEGER_TWO);
    assertThat(record.get(KEY_C2).toString()).isEqualTo(STRING_TWO);

    Assertions.assertThrows(EOFException.class, () -> reader.read(null, decoder));
  }

  //TODO: add test about writeUnit
  //TODO: add test about specified values
}
