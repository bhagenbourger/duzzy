package io.duzzy.plugin.serializer;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.serializer.Serializer;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class SqlSerializerTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File serializerFile = getFromResources(getClass(), "serializer/sql-serializer.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);

    assertThat(serializer).isInstanceOf(SqlSerializer.class);
  }

  @Test
  void serializeSqlWithDefaultValues() throws IOException {
    final String expected =
        "insert into duzzy_table values (1, 'one')"
            + "insert into duzzy_table values (2, 'two')";
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    final SqlSerializer sqlSerializer = new SqlSerializer(null);
    sqlSerializer.init(outputStream, DuzzyContext.DEFAULT.schemaContext());
    sqlSerializer.serialize(getDataOne());
    sqlSerializer.serialize(getDataTwo());
    sqlSerializer.close();

    assertThat(outputStream.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }

  @Test
  void serializeSqlWithCustomValues() throws IOException {
    final String expected =
        "insert into my_table values (1, 'one')"
            + "insert into my_table values (2, 'two')";
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    final File serializerFile = getFromResources(getClass(), "serializer/sql-serializer.yaml");
    final SqlSerializer sqlSerializer = YAML_MAPPER.readValue(serializerFile, SqlSerializer.class);
    sqlSerializer.init(outputStream, DuzzyContext.DEFAULT.schemaContext());
    sqlSerializer.serialize(getDataOne());
    sqlSerializer.serialize(getDataTwo());
    sqlSerializer.close();

    assertThat(outputStream.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }
}
