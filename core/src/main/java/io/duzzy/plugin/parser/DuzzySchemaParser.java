package io.duzzy.plugin.parser;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import java.io.File;
import java.io.IOException;

@Documentation(
    identifier = "io.duzzy.plugin.parser.DuzzySchemaParser",
    description = "DuzzySchema parser, it is the default parser, "
        + "if no parser is specified this parser is used.",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PARSER,
    nativeSupport = true
)
public class DuzzySchemaParser implements Parser {

  public DuzzySchema parse(File file, DuzzyConfig duzzyConfig) throws IOException {
    return YAML_MAPPER.readValue(file, DuzzySchema.class);
  }
}
