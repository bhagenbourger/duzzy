package io.duzzy.core.sink;

import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.serializer.SqlSerializer;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class JdbcSink extends Sink {

  private Connection connection;
  private Statement statement;
  private final boolean failOnError;

  public JdbcSink(
      SqlSerializer serializer,
      String url,
      String user,
      String password,
      Boolean failOnError
  ) {
    super(serializer, new ByteArrayOutputStream());
    this.failOnError = failOnError == null || failOnError;
    try {
      this.connection = DriverManager.getConnection(url, user, password);
      this.statement = connection.createStatement();
    } catch (SQLException e) {
      //TODO: log exception
    }
  }

  @Override
  public void write(DataItems data) throws Exception {
    super.write(data);
    try {
      statement.execute(((ByteArrayOutputStream) outputStream).toString(StandardCharsets.UTF_8));
    } catch (SQLException e) {
      //TODO: log exception
      if (failOnError) {
        throw e;
      }
    }
    ((ByteArrayOutputStream) outputStream).reset();
  }

  @Override
  public void close() throws Exception {
    super.close();
    if (statement != null) {
      statement.close();
    }
    if (connection != null) {
      connection.close();
    }
  }
}
