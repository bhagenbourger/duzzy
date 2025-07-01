package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.INTEGER_ONE;
import static io.duzzy.tests.Data.INTEGER_TWO;
import static io.duzzy.tests.Data.KEY_C1;
import static io.duzzy.tests.Data.KEY_C2;
import static io.duzzy.tests.Data.STRING_ONE;
import static io.duzzy.tests.Data.STRING_TWO;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.contrib.nio.testing.LocalStorageHelper;
import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.serializer.CsvSerializer;
import io.duzzy.plugin.serializer.ParquetSerializer;
import io.duzzy.tests.Data;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.io.LocalInputFile;
import org.junit.jupiter.api.Test;

public class GcsSinkTest {

  private static final String BUCKET_NAME = "duzzy-test";

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/gcs-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(GcsSink.class);
    assertThat(sink.getSerializer()).isInstanceOf(CsvSerializer.class);
  }

  @Test
  void writeData() throws Exception {
    try (final Storage storage = LocalStorageHelper.getOptions().getService()) {
      final String objectName = "test-object.csv";
      final GcsSink gcsSink = new GcsSink(
          new CsvSerializer(null, null, null),
          BUCKET_NAME,
          objectName,
          null,
          null,
          storage
      );

      gcsSink.init(null);

      gcsSink.write(Data.getDataOne());
      gcsSink.write(Data.getDataOne());
      gcsSink.write(Data.getDataTwo());

      gcsSink.close();

      final byte[] expected = """
          1,one
          1,one
          2,two
          """.getBytes(StandardCharsets.UTF_8);

      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      storage.downloadTo(BlobId.of(BUCKET_NAME, objectName), outputStream);
      assertThat(outputStream.toByteArray()).isEqualTo(expected);
    }
  }

  @Test
  void writeSeveralTimesThrowAnException() throws Exception {
    try (final Storage storage = LocalStorageHelper.getOptions().getService()) {
      final String objectName = "test-object-twice.csv";
      final GcsSink gcsSinkOne = new GcsSink(
          new CsvSerializer(null, null, null),
          BUCKET_NAME,
          objectName,
          null,
          null,
          storage
      );

      gcsSinkOne.init(null);
      gcsSinkOne.write(Data.getDataOne());
      gcsSinkOne.close();

      final GcsSink gcsSinkTwo = new GcsSink(
          new CsvSerializer(null, null, null),
          BUCKET_NAME,
          objectName,
          null,
          null,
          storage
      );

      assertThatThrownBy(() -> gcsSinkTwo.init(null))
          .isInstanceOf(StorageException.class);
    }
  }

  @Test
  void fork() throws Exception {
    try (final Storage storage = LocalStorageHelper.getOptions().getService()) {
      final String objectName = "test-object-fork.csv";
      final String objectNameForked = "test-object-fork_1.csv";
      final GcsSink gcsSink = new GcsSink(
          new CsvSerializer(null, null, null),
          BUCKET_NAME,
          objectName,
          null,
          null,
          storage
      );

      gcsSink.init(null);
      gcsSink.write(Data.getDataOne());
      gcsSink.write(Data.getDataTwo());
      gcsSink.close();

      final Sink forkedSink = gcsSink.fork(1L);

      forkedSink.init(null);
      forkedSink.write(Data.getDataOne());
      forkedSink.write(Data.getDataTwo());
      forkedSink.close();

      final byte[] expected = """
          1,one
          2,two
          """.getBytes(StandardCharsets.UTF_8);

      final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      storage.downloadTo(BlobId.of(BUCKET_NAME, objectName), outputStream);
      assertThat(outputStream.toByteArray()).isEqualTo(expected);

      final ByteArrayOutputStream outputStreamForked = new ByteArrayOutputStream();
      storage.downloadTo(BlobId.of(BUCKET_NAME, objectNameForked), outputStreamForked);
      assertThat(outputStreamForked.toByteArray()).isEqualTo(expected);
    }
  }

  @Test
  void writeParquetData() throws Exception {
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
    final String objectName = "test-parquet.parquet";
    try (final Storage storage = LocalStorageHelper.getOptions().getService()) {
      final GcsSink gcsSink = new GcsSink(
          new ParquetSerializer(null, null, null),
          BUCKET_NAME,
          objectName,
          null,
          null,
          storage
      );
      gcsSink.init(new DuzzySchema(Optional.empty(), fields));
      gcsSink.write(Data.getDataOne());
      gcsSink.write(Data.getDataTwo());
      gcsSink.close();

      final Path tempFile = Files.createTempFile("parquet-test", ".parquet");
      storage.downloadTo(BlobId.of(BUCKET_NAME, objectName), tempFile);

      try (
          final ParquetReader<GenericRecord> reader = AvroParquetReader
              .<GenericRecord>builder(new LocalInputFile(tempFile))
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
}
