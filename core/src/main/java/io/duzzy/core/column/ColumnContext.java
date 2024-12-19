package io.duzzy.core.column;

import io.duzzy.core.provider.Provider;
import java.util.List;
import java.util.Random;

public record ColumnContext(
    List<Provider<?>> providers,
    Boolean hasSchema,
    Random random,
    Long rowId,
    Long rowIndex
) {
}