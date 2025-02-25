package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.jooq.conf.ParamType;
import org.jooq.impl.DSL;

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
  protected void serialize(DataItems data, OutputStream writer) throws IOException {
    final String sql = DSL
        .insertInto(DSL.table(tableName))
        .values(data.toValues())
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
