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
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresqlSinkTest {

  private static final String CREATE_TABLE = "CREATE TABLE test(id INTEGER, name VARCHAR);";
  private static final String DROP_TABLE = "DROP TABLE test;";
  private static final String COUNT = "SELECT count(*) AS rows FROM test;";

  private static final PostgreSQLContainer<?> postgresql =
      new PostgreSQLContainer<>("postgres:16-alpine");

  @BeforeAll
  static void beforeAll() throws SQLException {
    postgresql.start();
    try (
        final Connection connection = getConnection();
        final Statement statement = connection.createStatement()
    ) {
      statement.execute(CREATE_TABLE);
    }
  }

  @AfterAll
  static void afterAll() throws SQLException {
    try (
        final Connection connection = getConnection();
        final Statement statement = connection.createStatement()
    ) {
      statement.execute(DROP_TABLE);
    }
    postgresql.stop();
  }

  private static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(
        postgresql.getJdbcUrl(),
        postgresql.getUsername(),
        postgresql.getPassword()
    );
  }

  @Test
  void parsedFromYaml() throws IOException {
    final File sinkFile = getFromResources(getClass(), "sink/postgresql-sink.yaml");
    final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

    assertThat(sink).isInstanceOf(PostgresqlSink.class);
    assertThat(sink.getSerializer()).isInstanceOf(SqlSerializer.class);
  }

  @Test
  void writeData() throws Exception {
    final PostgresqlSink postgresqlSink = new PostgresqlSink(
        new SqlSerializer("test"),
        postgresql.getJdbcUrl(),
        postgresql.getUsername(),
        postgresql.getPassword(),
        null
    );

    postgresqlSink.init(null);

    postgresqlSink.write(Data.getDataOne());
    postgresqlSink.write(Data.getDataOne());
    postgresqlSink.write(Data.getDataOne());

    try (
        final Connection connection = getConnection();
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(COUNT)
    ) {
      resultSet.next();
      assertThat(resultSet.getInt("rows")).isEqualTo(3);
    }
  }

}
