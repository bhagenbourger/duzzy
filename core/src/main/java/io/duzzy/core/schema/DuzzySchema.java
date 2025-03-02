package io.duzzy.core.schema;

import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.field.Field;
import java.util.List;

@Documentation(
    identifier = "io.duzzy.core.schema.DuzzySchema",
    description = "Duzzy schema is the default input schema "
        + "in which you can specify whatever you want into the output",
    duzzyType = DuzzyType.DUZZY_SCHEMA,
    parameters = {},
    example = ""
)
public record DuzzySchema(
    List<Field> fields
) {
}
