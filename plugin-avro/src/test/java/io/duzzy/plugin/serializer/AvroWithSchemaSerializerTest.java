package io.duzzy.plugin.serializer;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.column.Column;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.tests.Data;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.SeekableByteArrayInput;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class AvroWithSchemaSerializerTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File serializerFile = getFromResources(getClass(), "serializer/avro-with-schema-serializer.yaml");
        final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

        assertThat(serializer).isInstanceOf(AvroWithSchemaSerializer.class);
    }

    @Test
    void writeWithDefaultValues() throws IOException {
        final String expectedSchema = "{\"type\":\"record\",\"name\":\"name\",\"namespace\":\"namespace\",\"fields\":[{\"name\":\"c1\",\"type\":\"int\"},{\"name\":\"c2\",\"type\":\"string\"}]}";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final List<Column> columns = List.of(
                new Column(
                        "c1",
                        ColumnType.INTEGER,
                        null,
                        List.of(new IntegerIncrementProvider(null, null))
                ),
                new Column(
                        "c2",
                        ColumnType.STRING,
                        null,
                        List.of(new AlphanumericRandomProvider())
                )
        );
        final DuzzyContext duzzyContext = new DuzzyContext(null, columns, null, null, null);

        final AvroWithSchemaSerializer avroWithSchemaSerializer =
                new AvroWithSchemaSerializer(null, null);
        avroWithSchemaSerializer.init(outputStream, duzzyContext);
        avroWithSchemaSerializer.writeAll(Data.getDataOne());
        avroWithSchemaSerializer.writeAll(Data.getDataTwo());
        avroWithSchemaSerializer.close();

        final DataFileReader<GenericData.Record> records = new DataFileReader<>(
                new SeekableByteArrayInput(outputStream.toByteArray()),
                new GenericDatumReader<>()
        );
        assertThat(records.getSchema().toString()).isEqualTo(expectedSchema);

        GenericData.Record record = records.next();
        assertThat(record.get("c1")).isEqualTo(1);
        assertThat(record.get("c2").toString()).isEqualTo("one");

        record = records.next();
        assertThat(record.get("c1")).isEqualTo(2);
        assertThat(record.get("c2").toString()).isEqualTo("two");

        assertThat(records.hasNext()).isFalse();
    }

    //TODO: add test about schema
    //TODO: add test about writeUnit
    //TODO: add test about specified values
}
