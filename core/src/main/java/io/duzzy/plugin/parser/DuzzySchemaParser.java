package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzyInputSchema;
import io.duzzy.core.schema.DuzzySchema;

import java.io.File;
import java.io.IOException;

public class DuzzySchemaParser implements Parser {

    public DuzzyContext parse(File file, DuzzyConfig duzzyConfig) throws IOException {
        final DuzzySchema duzzySchema = YAML_MAPPER.readValue(file, DuzzySchema.class);
        return new DuzzyContext(
                new DuzzyInputSchema(duzzySchema),
                duzzySchema.columns(),
                duzzySchema.sink(),
                duzzySchema.rows(),
                duzzySchema.seed()
        );
    }
}
