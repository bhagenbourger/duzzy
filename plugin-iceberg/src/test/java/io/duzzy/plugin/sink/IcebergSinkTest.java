package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.plugin.sink.IcebergSink.CATALOG_NAME;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.deleteDirectory;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.projectnessie.testing.nessie.NessieContainer.builder;

import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.iceberg.CatalogType;
import io.duzzy.plugin.serializer.IcebergParquetSerializer;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import org.apache.iceberg.CatalogProperties;
import org.apache.iceberg.data.GenericRecord;
import org.apache.iceberg.data.IcebergGenerics;
import org.apache.iceberg.data.Record;
import org.apache.iceberg.io.CloseableIterable;
import org.apache.iceberg.jdbc.JdbcCatalog;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.projectnessie.testing.nessie.NessieContainer;
import org.testcontainers.containers.MySQLContainer;

public class IcebergSinkTest {

  private static final String WAREHOUSE_LOCATION = "/tmp/iceberg_warehouse";
  private static final Map<CatalogType, Map<String, String>> CATALOG_PROPERTIES = new HashMap<>();

  private static final NessieContainer NESSIE_CONTAINER = new NessieContainer(builder().build());
  private static final MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:5.7.34");

  @BeforeAll
  static void setUp() {
    NESSIE_CONTAINER.start();
    MYSQL_CONTAINER.start();
    final Map<String, String> hadoopProperties = Map.of(
        CATALOG_NAME, "hadoop_catalog",
        CatalogProperties.WAREHOUSE_LOCATION, WAREHOUSE_LOCATION
    );
    final Map<String, String> hiveProperties = Map.of(
        CATALOG_NAME, "hive_catalog",
        CatalogProperties.WAREHOUSE_LOCATION, WAREHOUSE_LOCATION,
        CatalogProperties.URI, "thrift://metastore:9083"
    );
    final Map<String, String> jdbcProperties = Map.of(
        CATALOG_NAME, "jdbc_catalog",
        CatalogProperties.WAREHOUSE_LOCATION, WAREHOUSE_LOCATION,
        CatalogProperties.URI, MYSQL_CONTAINER.getJdbcUrl(),
        JdbcCatalog.PROPERTY_PREFIX + "user", MYSQL_CONTAINER.getUsername(),
        JdbcCatalog.PROPERTY_PREFIX + "password", MYSQL_CONTAINER.getPassword()
    );
    final Map<String, String> nessieProperties = Map.of(
        CATALOG_NAME, "nessie_catalog",
        CatalogProperties.WAREHOUSE_LOCATION, WAREHOUSE_LOCATION,
        CatalogProperties.URI, NESSIE_CONTAINER.getExternalNessieUri().toString()
    );
    CATALOG_PROPERTIES.put(CatalogType.HADOOP, hadoopProperties);
    CATALOG_PROPERTIES.put(CatalogType.HIVE, hiveProperties);
    CATALOG_PROPERTIES.put(CatalogType.JDBC, jdbcProperties);
    CATALOG_PROPERTIES.put(CatalogType.NESSIE, nessieProperties);
  }

  @AfterAll
  static void cleanUp() throws IOException {
    NESSIE_CONTAINER.stop();
    MYSQL_CONTAINER.stop();
    deleteDirectory(Paths.get(WAREHOUSE_LOCATION));
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/iceberg-sink.yaml");
    final Sink<?> sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    Assertions.assertThat(sink).isInstanceOf(IcebergSink.class);
    Assertions.assertThat(sink.getSerializer()).isInstanceOf(IcebergParquetSerializer.class);
  }

  @ParameterizedTest
  @EnumSource(value = CatalogType.class, names = {"HADOOP", "NESSIE", "JDBC"})
  void writeData(CatalogType catalogType) throws Exception {
    final File file = getFromResources(getClass(), "schema/schema.avsc");
    final IcebergSink sink = new IcebergSink(
        new IcebergParquetSerializer("my_name", "my_namespace", file),
        "webapp",
        "user_events",
        catalogType,
        CATALOG_PROPERTIES.getOrDefault(catalogType, Map.of())
    );
    sink.init(null, 3L);
    sink.write(getDataTwo());
    sink.write(getDataTwo());
    sink.write(getDataTwo());
    sink.close();

    final IcebergSink fork = sink.fork(1L);
    fork.init(null, 3L);
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

