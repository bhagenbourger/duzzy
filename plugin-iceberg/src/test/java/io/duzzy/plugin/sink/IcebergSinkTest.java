package io.duzzy.plugin.sink;

import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;

import io.duzzy.plugin.serializer.IcebergParquetSerializer;
import java.io.File;
import java.util.UUID;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.CloseableIterable;
import org.junit.jupiter.api.Test;

public class IcebergSinkTest {

  @Test
  void testIcebergSink() throws Exception {
    final File file = getFromResources(getClass(), "schema.avsc");
    final IcebergSink sink = new IcebergSink(
        new IcebergParquetSerializer("my_name", "my_namespace", file),
        "webapp",
        "user_events",
        "/tmp/test-iceberg-data/user_events-" + UUID.randomUUID()
    );
    sink.init(null);
    sink.write(getDataOne());
    sink.write(getDataTwo());
    sink.write(getDataTwo());
    sink.write(getDataTwo());
    sink.close();

    System.out.println(sink.getIcebergTable().location());
    final CloseableIterable<Record> result = IcebergGenerics.read(sink.getIcebergTable()).build();
    for (Record r : result) {
      System.out.println(r);
      System.out.println(r.get(0));
      System.out.println(r.get(1));
    }
  }
}
