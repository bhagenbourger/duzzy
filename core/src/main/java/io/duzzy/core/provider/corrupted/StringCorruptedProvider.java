package io.duzzy.core.provider.corrupted;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

public interface StringCorruptedProvider extends Provider<String> {

    static String corruptedValue(ColumnContext columnContext, Integer maxLength) {
        final int length = columnContext.random().nextInt(0, maxLength + 1);
        return columnContext
                .random()
                .ints(0, 123)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
