package io.duzzy.core.field;

import io.duzzy.core.provider.Provider;
import java.util.List;
import java.util.Random;

public record FieldContext(
    List<Provider<?>> providers,
    Boolean hasSchema,
    Random random,
    Long rowId,
    Long rowIndex
) {
}