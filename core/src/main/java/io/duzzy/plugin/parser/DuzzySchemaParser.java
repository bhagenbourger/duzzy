package io.duzzy.plugin.parser;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.schema.SchemaContext;
import java.io.File;
import java.io.IOException;

public class DuzzySchemaParser implements Parser {

  public SchemaContext parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    final DuzzySchema duzzySchema = YAML_MAPPER.readValue(file, DuzzySchema.class);
    return new SchemaContext(duzzySchema.fields());
  }
}
