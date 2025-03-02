package io.duzzy.plugin.parser;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.schema.SchemaContext;
import java.io.File;
import java.io.IOException;

@Documentation(
    identifier = "io.duzzy.plugin.parser.DuzzySchemaParser",
    description = "DuzzySchema parser, it is the default parser, "
        + "if no parser is specified this parser is used.",
    duzzyType = DuzzyType.PARSER,
    parameters = {},
    example = ""
)
public class DuzzySchemaParser implements Parser {

  public SchemaContext parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    final DuzzySchema duzzySchema = YAML_MAPPER.readValue(file, DuzzySchema.class);
    return new SchemaContext(duzzySchema.fields());
  }
}
