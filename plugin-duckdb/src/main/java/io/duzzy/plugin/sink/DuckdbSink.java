package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.sink.JdbcSink;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.SqlSerializer;

public final class DuckdbSink extends JdbcSink {

  @JsonCreator
  public DuckdbSink(
      @JsonProperty("serializer") SqlSerializer serializer,
      @JsonProperty("url") String url,
      @JsonProperty("user") String user,
      @JsonProperty("password") String password,
      @JsonProperty("fail_on_error")
      @JsonAlias({"failOnError", "fail-on-error"})
      Boolean failOnError
  ) {
    super(serializer, url, user, password, failOnError);
  }

  @Override
  public Sink fork(Long threadId) throws Exception {
    return new DuckdbSink(
        (SqlSerializer) serializer.fork(threadId),
        url,
        user,
        password,
        failOnError
    );
  }
}
