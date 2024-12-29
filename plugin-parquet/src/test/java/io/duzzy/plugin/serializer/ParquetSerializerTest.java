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
import static io.duzzy.tests.Helper.createTempFile;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroReadSupport;
import org.apache.parquet.hadoop.ParquetReader;
import org.junit.jupiter.api.Test;

public class ParquetSerializerTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File serializerFile = getFromResources(getClass(), "serializer/parquet-serializer.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);


    assertThat(serializer).isInstanceOf(ParquetSerializer.class);
  }

  @Test
  void serializeWithDefaultValues() throws IOException {
    final File file = createTempFile(getClass().getSimpleName());
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
    final SchemaContext schemaContext = new SchemaContext(null, columns);

    try (final OutputStream outputStream = new FileOutputStream(file)) {
      final ParquetSerializer parquetSerializer = new ParquetSerializer(null, null);
      parquetSerializer.init(outputStream, schemaContext);
      parquetSerializer.serialize(getDataOne());
      parquetSerializer.serialize(getDataTwo());
      parquetSerializer.close();
    }

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
