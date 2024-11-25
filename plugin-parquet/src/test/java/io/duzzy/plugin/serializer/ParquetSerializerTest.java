package io.duzzy.plugin.serializer;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.column.Column;
import io.duzzy.core.provider.ColumnType;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroReadSupport;
import org.apache.parquet.hadoop.ParquetReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.*;
import static io.duzzy.tests.Helper.createTempFile;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class ParquetSerializerTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File serializerFile = getFromResources(getClass(), "serializer/parquet-serializer.yaml");
        final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);


        assertThat(serializer).isInstanceOf(ParquetSerializer.class);
    }

    @Test
    void writeWithDefaultValues() throws IOException {
        final File file = createTempFile(getClass().getSimpleName());
        final OutputStream outputStream = new FileOutputStream(file);
        final List<Column> columns = List.of(
                new Column(
                        KEY_C1,
                        ColumnType.INTEGER,
                        null,
                        List.of(new IntegerIncrementProvider(null, null))
                ),
                new Column(
                        KEY_C2,
                        ColumnType.STRING,
                        null,
                        List.of(new AlphanumericRandomProvider())
                )
        );
        final DuzzyContext duzzyContext = new DuzzyContext(null, columns, null, null, null);

        final ParquetSerializer parquetSerializer = new ParquetSerializer(null, null);
        parquetSerializer.init(outputStream, duzzyContext);
        parquetSerializer.writeAll(getDataOne());
        parquetSerializer.writeAll(getDataTwo());
        parquetSerializer.close();

        final ParquetReader<GenericRecord> reader = AvroParquetReader
                .<GenericRecord>builder(new AvroReadSupport<>(), new Path(file.getPath()))
                .build();
        GenericRecord record = reader.read();
        assertThat(record.get(KEY_C1)).isEqualTo(INTEGER_ONE);
        assertThat(record.get(KEY_C2).toString()).isEqualTo(STRING_ONE);

        record = reader.read();
        assertThat(record.get(KEY_C1)).isEqualTo(INTEGER_TWO);
        assertThat(record.get(KEY_C2).toString()).isEqualTo(STRING_TWO);

        record = reader.read();
        assertThat(record).isNull();
    }
}
