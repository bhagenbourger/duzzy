package io.duzzy.core.sink;

import io.duzzy.core.DuzzyRow;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.plugin.serializer.SqlSerializer;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JdbcSink extends Sink {

  private static final Logger logger = LoggerFactory.getLogger(JdbcSink.class);

  protected final String url;
  protected final String user;
  protected final String password;
  protected final Boolean failOnError;
  private Connection connection;
  private Statement statement;
  private final ByteArrayOutputStream outputStream;
  private long currentSize = 0;

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
    this.outputStream = new ByteArrayOutputStream();
    try {
      this.connection = DriverManager.getConnection(url, user, password);
      this.statement = connection.createStatement();
    } catch (SQLException e) {
      logger.error("Error while creating JDBC connection", e);
    }
  }

  @Override
  public long size() {
    return currentSize;
  }

  @Override
  public void init(DuzzySchema duzzySchema) throws Exception {
    getSerializer().init(outputStream, duzzySchema);
  }

  @Override
  public void write(DuzzyRow data) throws Exception {
    super.write(data);
    try {
      statement.execute(outputStream.toString(StandardCharsets.UTF_8));
    } catch (SQLException e) {
      logger.warn("Error while writing data", e);
      if (failOnError) {
        throw e;
      }
    }
    currentSize += data.sizeOfValues();
    outputStream.reset();
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
