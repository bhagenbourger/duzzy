package io.duzzy.plugin.sink;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.sink.JdbcSink;
import io.duzzy.core.sink.OutputStreamSink;
import io.duzzy.plugin.serializer.SqlSerializer;
import io.duzzy.tests.Data;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import org.hsqldb.server.Server;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JdbcSinkTest {

  private static final Server SERVER = new org.hsqldb.Server();
  private static final String TABLE_NAME = "my_table";
  private static final String URL = "jdbc:hsqldb:mem:my_database";

  @BeforeAll
  static void beforeAll() throws ClassNotFoundException, SQLException {
    Class.forName("org.hsqldb.jdbc.JDBCDriver");

    SERVER.setDatabaseName(0, "my_database");
    SERVER.setDatabasePath(0, "mem:my_database");
    SERVER.start();

    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement()
    ) {
      statement.execute(
          "CREATE TABLE " + TABLE_NAME + " ("
              + " id int, "
              + " name varchar, "
              + " PRIMARY KEY (id) "
              + " )"
      );
    }
  }

  @AfterAll
  static void afterAll() {
    SERVER.shutdown();
  }

  @BeforeEach
  void setUp() throws SQLException {
    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement()
    ) {
      statement.execute("TRUNCATE TABLE " + TABLE_NAME);
    }
  }

  @Test
  void writeData() throws Exception {
    final HsqldbSink sink = new HsqldbSink(
        new SqlSerializer(TABLE_NAME),
        URL,
        null,
        null,
        true
    );

    sink.init(null, 2L);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME)
    ) {
      final boolean first = resultSet.next();
      final int firstId = resultSet.getInt(1);
      final String firstName = resultSet.getString(2);
      final boolean second = resultSet.next();
      final int secondId = resultSet.getInt(1);
      final String secondName = resultSet.getString(2);
      final boolean third = resultSet.next();

      assertThat(first).isTrue();
      assertThat(second).isTrue();
      assertThat(third).isFalse();

      assertThat(firstId).isEqualTo(1);
      assertThat(firstName).isEqualTo("one");

      assertThat(secondId).isEqualTo(2);
      assertThat(secondName).isEqualTo("two");

      final String q1 = "INSERT INTO " + TABLE_NAME + " VALUES (1, 'one')";
      final String q2 = "INSERT INTO " + TABLE_NAME + " VALUES (2, 'two')";
      final int size =
          q1.getBytes(StandardCharsets.UTF_8).length + q2.getBytes(StandardCharsets.UTF_8).length;
      assertThat(sink.getSerializer().size()).isEqualTo(size);
    }
  }

  @Test
  void failWhenIntegrityConstraintNotRespected() throws Exception {
    final HsqldbSink sink = new HsqldbSink(
        new SqlSerializer(TABLE_NAME),
        URL,
        null,
        null,
        true
    );

    sink.init(null, 1L);
    sink.write(Data.getDataOne());
    assertThatThrownBy(() -> sink.write(Data.getDataOne()))
        .isInstanceOf(SQLIntegrityConstraintViolationException.class)
        .hasMessage(
            "integrity constraint violation: "
                + "unique constraint or index violation ; "
                + "SYS_PK_10091 table: MY_TABLE"
        );
    sink.close();
  }

  @Test
  void notFailWhenIntegrityConstraintNotRespected() throws Exception {
    final HsqldbSink sink = new HsqldbSink(
        new SqlSerializer(TABLE_NAME),
        URL,
        null,
        null,
        false
    );

    sink.init(null, 3L);
    sink.write(Data.getDataOne());
    sink.write(Data.getDataOne());
    sink.write(Data.getDataTwo());
    sink.close();

    try (
        final Connection connection = DriverManager.getConnection(URL);
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(
            "SELECT count(*) as COUNT_ROWS FROM " + TABLE_NAME
        )
    ) {
      resultSet.next();
      final int rows = resultSet.getInt(1);

      assertThat(rows).isEqualTo(2);
    }
  }

  private static class HsqldbSink extends JdbcSink {
    public HsqldbSink(
        SqlSerializer serializer,
        String url,
        String user,
        String password,
        Boolean failOnError
    ) {
      super(serializer, url, user, password, failOnError);
    }

    @Override
    public OutputStreamSink fork(Long threadId) {
      return new HsqldbSink((SqlSerializer) serializer, url, user, password, failOnError);
    }
  }
}
