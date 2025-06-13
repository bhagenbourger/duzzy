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
    identifier = "io.duzzy.plugin.sink.MysqlSink",
    description = "Sink data to a MySQL database",
    module = "io.duzzy.plugin-mysql",
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
        identifier: "io.duzzy.plugin.sink.MysqlSink"
        serializer:
          identifier: "io.duzzy.plugin.serializer.SqlSerializer"
          table_name: "my_table"
        url: "jdbc:mysql://localhost:3306/my_database"
        user: "user"
        password: "password"
        fail_on_error: true
        """
)
public class MysqlSink extends JdbcSink {

  @JsonCreator
  public MysqlSink(
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
  public MysqlSink fork(Long threadId) throws Exception {
    return new MysqlSink(
        (SqlSerializer) serializer.fork(threadId),
        url,
        user,
        password,
        failOnError
    );
  }
}
