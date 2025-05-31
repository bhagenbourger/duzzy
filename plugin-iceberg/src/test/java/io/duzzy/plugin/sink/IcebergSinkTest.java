package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.deleteDirectory;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.iceberg.CatalogType;
import io.duzzy.plugin.serializer.IcebergParquetSerializer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.CloseableIterable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class IcebergSinkTest {

  private static final String WAREHOUSE_LOCATION = "/tmp/iceberg_warehouse";

  @AfterAll
  static void cleanUp() throws IOException {
    deleteDirectory(Paths.get(WAREHOUSE_LOCATION));
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/iceberg-sink.yaml");
    final Sink<?> sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    Assertions.assertThat(sink).isInstanceOf(IcebergSink.class);
    Assertions.assertThat(sink.getSerializer()).isInstanceOf(IcebergParquetSerializer.class);
  }

  @Test
  void writeData() throws Exception {
    final File file = getFromResources(getClass(), "schema/schema.avsc");
    final IcebergSink sink = new IcebergSink(
        new IcebergParquetSerializer("my_name", "my_namespace", file),
        "webapp",
        "user_events",
        WAREHOUSE_LOCATION,
        CatalogType.HADOOP
    );
    sink.init(null);
    sink.write(getDataTwo());
    sink.write(getDataTwo());
    sink.write(getDataTwo());
    sink.close();

    final IcebergSink fork = sink.fork(1L);
    fork.init(null);
    fork.write(getDataTwo());
    fork.close();

    long size = sink.getSerializer().size() + fork.getSerializer().size();
    assertThat(size).isEqualTo(1426L);

    final GenericRecord expected = GenericRecord.create(sink.getSerializer().getSchema());
    expected.setField("c1", 2);
    expected.setField("c2", "two");

    try (final CloseableIterable<Record> result = IcebergGenerics
        .read(fork.getIcebergTable())
        .project(fork.getSerializer().getSchema())
        .build()) {
      int i = 0;
      for (Record record : result) {
        assertThat(record).isEqualTo(expected);
        i++;
      }
      assertThat(i).isEqualTo(4);
    }
  }
}

