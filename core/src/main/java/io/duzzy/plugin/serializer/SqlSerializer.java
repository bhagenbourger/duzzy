package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.SqlSerializer",
    description = "Serialize data in SQL",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.SERIALIZER,
    parameters = {
        @Parameter(
            name = "table_name",
            aliases = {"tableName", "table-name"},
            description = "The name of the table to insert data"
        )
    },
    example = """
        ---
        identifier: "io.duzzy.plugin.serializer.SqlSerializer"
        table_name: "my_table"
        """
)
public class SqlSerializer extends Serializer<OutputStream> {

  private final String tableName;

  @JsonCreator
  public SqlSerializer(
      @JsonProperty("table_name")
      @JsonAlias({"tableName", "table-name"})
      String tableName
  ) {
    this.tableName = tableName;
  }

  @Override
  protected OutputStream buildWriter(OutputStream outputStream) throws IOException {
    return outputStream;
  }

  @Override
  protected void serialize(DuzzyRow row, OutputStream writer) throws IOException {
    final String sql = DSL
        .insertInto(DSL.table(tableName))
        .values(row.toValues())
        .getSQL(ParamType.INLINED);
    writer.write(sql.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public Boolean hasSchema() {
    return true;
  }

  @Override
  public SqlSerializer fork(Long threadId) {
    return new SqlSerializer(tableName);
  }
}
