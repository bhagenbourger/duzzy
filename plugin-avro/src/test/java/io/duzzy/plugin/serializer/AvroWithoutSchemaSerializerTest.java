package io.duzzy.plugin.serializer;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.column.Column;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.*;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class AvroWithoutSchemaSerializerTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File serializerFile = getFromResources(getClass(), "serializer/avro-without-schema-serializer.yaml");
        final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

        assertThat(serializer).isInstanceOf(AvroWithoutSchemaSerializer.class);
    }

    @Test
    void writeWithDefaultValues() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final List<Column> columns = List.of(
                new Column(
                        KEY_C1,
                        ColumnType.INTEGER,
                        null,
                        null,
                        List.of(new IntegerIncrementProvider(null, null))
                ),
                new Column(
                        KEY_C2,
                        ColumnType.STRING,
                        null,
                        null,
                        List.of(new AlphanumericRandomProvider())
                )
        );
        final DuzzyContext duzzyContext = new DuzzyContext(null, columns, null, null, null);

        final AvroWithoutSchemaSerializer avroWithoutSchemaSerializer =
                new AvroWithoutSchemaSerializer(null, null);
        avroWithoutSchemaSerializer.init(outputStream, duzzyContext);
        avroWithoutSchemaSerializer.writeAll(getDataOne());
        avroWithoutSchemaSerializer.writeAll(getDataTwo());

        final DatumReader<GenericData.Record> reader = new GenericDatumReader<>();
        reader.setSchema(avroWithoutSchemaSerializer.getSchema());
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

    //TODO: add test about schema
    //TODO: add test about writeUnit
    //TODO: add test about specified values
}
