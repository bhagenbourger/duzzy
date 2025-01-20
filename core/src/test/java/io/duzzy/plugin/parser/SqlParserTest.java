package io.duzzy.plugin.parser;

import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.plugin.provider.random.IntegerRandomProvider;
import java.io.File;
import java.io.IOException;
import org.jooq.impl.ParserException;
import org.junit.jupiter.api.Test;

public class SqlParserTest {

  @Test
  void parsedFromFile() throws IOException {
    final File sqlFile = getFromResources(getClass(), "schema/table.sql");
    final SchemaContext schemaContext = new SqlParser().parse(sqlFile, null);

    assertThat(schemaContext.fields()).hasSize(8);
    final Field first = schemaContext.fields().getFirst();
    assertThat(first.name()).isEqualTo("I");
    assertThat(first.type()).isEqualTo(Type.INTEGER);
    assertThat(first.nullRate()).isEqualTo(0.1f);
    assertThat(first.providers().getFirst()).isInstanceOf(IntegerRandomProvider.class);
  }

  @Test
  void invalidSqlSchema() {
    final File sqlSchema = getFromResources(getClass(), "schema/duzzy-schema-xml.yaml");
    assertThatThrownBy(() -> new SqlParser().parse(sqlSchema, null)).isInstanceOf(
        ParserException.class);
  }


}
