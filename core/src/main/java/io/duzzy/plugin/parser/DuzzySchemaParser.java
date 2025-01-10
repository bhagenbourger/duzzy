package io.duzzy.plugin.parser;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzySchema;
import java.io.File;
import java.io.IOException;

public class DuzzySchemaParser implements Parser {

  public DuzzySchema parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    return YAML_MAPPER.readValue(file, DuzzySchema.class);
  }
}
