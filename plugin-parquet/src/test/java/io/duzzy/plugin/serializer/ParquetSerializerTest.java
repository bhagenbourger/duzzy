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

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
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

    try (final OutputStream outputStream = new FileOutputStream(file)) {
      final ParquetSerializer parquetSerializer = new ParquetSerializer(null, null, null);
      parquetSerializer.init(outputStream, new DuzzySchema(Optional.empty(), fields), 2L);
      parquetSerializer.serialize(getDataOne());
      parquetSerializer.serialize(getDataTwo());
      parquetSerializer.close();

      assertThat(parquetSerializer.size()).isEqualTo(file.length());
    }

    try (
        final ParquetReader<GenericRecord> reader = AvroParquetReader
            .<GenericRecord>builder(new AvroReadSupport<>(), new Path(file.getPath()))
            .build()
    ) {
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
}
