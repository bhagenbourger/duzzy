package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.plugin.serializer.SqlSerializer;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JdbcSink extends OutputStreamSink {

  private static final Logger logger = LoggerFactory.getLogger(JdbcSink.class);

  protected final String url;
  protected final String user;
  protected final String password;
  protected final Boolean failOnError;
  private Connection connection;
  private Statement statement;

  public JdbcSink(
      SqlSerializer serializer,
      String url,
      String user,
      String password,
      Boolean failOnError
  ) {
    super(serializer);
    this.url = url;
    this.user = user;
    this.password = password;
    this.failOnError = failOnError == null || failOnError;
    try {
      this.connection = DriverManager.getConnection(url, user, password);
      this.statement = connection.createStatement();
    } catch (SQLException e) {
      logger.error("Error while creating JDBC connection", e);
    }
  }

  @Override
  public OutputStream outputSupplier() {
    return new ByteArrayOutputStream();
  }

  @Override
  public void write(DuzzyRow data) throws Exception {
    super.write(data);
    try {
      statement.execute(((ByteArrayOutputStream) getOutput()).toString(StandardCharsets.UTF_8));
    } catch (SQLException e) {
      logger.warn("Error while writing data", e);
      if (failOnError) {
        throw e;
      }
    }
    ((ByteArrayOutputStream) getOutput()).reset();
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
