package io.duzzy.core.schema;

import io.duzzy.core.field.Field;
import java.util.List;

public record DuzzySchema(
    List<Field> fields
) {
}
