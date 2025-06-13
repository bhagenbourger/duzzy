package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.sink.JdbcSink;
import io.duzzy.plugin.serializer.SqlSerializer;

@Documentation(
    identifier = "io.duzzy.plugin.sink.PostgresqlSink",
    description = "Sink data to a PostgreSQL database",
    module = "io.duzzy.plugin-postgresql",
    duzzyType = DuzzyType.SINK,
    parameters = {
        @Parameter(
            name = "serializer",
            description = "The serializer to use"
        ),
        @Parameter(
            name = "url",
            description = "The JDBC URL"
        ),
        @Parameter(
            name = "user",
            description = "The user"
        ),
        @Parameter(
            name = "password",
            description = "The password"
        ),
        @Parameter(
            name = "fail_on_error",
            aliases = {"failOnError", "fail-on-error"},
            description = "Whether to fail on error"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.sink.PostgresqlSink"
        serializer:
          identifier: "io.duzzy.plugin.serializer.SqlSerializer"
          table_name: "my_table"
        url: "jdbc:postgresql://localhost:5432/my_database"
        user: "user"
        password: "password"
        fail_on_error: true
        """
)
public class PostgresqlSink extends JdbcSink {

  @JsonCreator
  public PostgresqlSink(
      @JsonProperty("serializer")
      SqlSerializer serializer,
      @JsonProperty("url")
      String url,
      @JsonProperty("user")
      String user,
      @JsonProperty("password")
      String password,
      @JsonProperty("fail_on_error")
      @JsonAlias({"failOnError", "fail-on-error"})
      Boolean failOnError
  ) {
    super(serializer, url, user, password, failOnError);
  }

  @Override
  public PostgresqlSink fork(Long threadId) throws Exception {
    return new PostgresqlSink(
        (SqlSerializer) serializer.fork(threadId),
        url,
        user,
        password,
        failOnError
    );
  }
}
