package io.duzzy.plugin.sink;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.SqlSerializer;
import io.duzzy.tests.Data;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DuckdbSinkTest {

  private static final String URL = "jdbc:duckdb:/tmp/my_database";
  private static final String CREATE_TABLE = "CREATE TABLE test(id INTEGER, name STRING);";
  private static final String DROP_TABLE = "DROP TABLE test;";
  private static final String COUNT = "SELECT count(*) AS rows FROM test;";

  @BeforeAll
  static void beforeAll() throws SQLException {
    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement()
    ) {
      statement.execute(CREATE_TABLE);
    }
  }

  @AfterAll
  static void afterAll() throws SQLException {
    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement()
    ) {
      statement.execute(DROP_TABLE);
    }
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/duckdb-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(DuckdbSink.class);
    assertThat(sink.getSerializer()).isInstanceOf(SqlSerializer.class);
  }

  @Test
  void writeData() throws Exception {
    final DuckdbSink duckdbSink = new DuckdbSink(
        new SqlSerializer("test"),
        URL,
        null,
        null,
        null
    );

    duckdbSink.init(null);

    duckdbSink.write(Data.getDataOne());
    duckdbSink.write(Data.getDataOne());
    duckdbSink.write(Data.getDataOne());

    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement()
    ) {
      final ResultSet resultSet = statement.executeQuery(COUNT);
      resultSet.next();
      assertThat(resultSet.getInt("rows")).isEqualTo(3);
    }
  }
}
