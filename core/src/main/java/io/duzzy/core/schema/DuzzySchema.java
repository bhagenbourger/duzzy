package io.duzzy.core.schema;

import io.duzzy.core.field.Field;
import io.duzzy.core.sink.Sink;
import java.util.List;

public record DuzzySchema(
    List<Field> fields,
    Sink sink
) {
}
