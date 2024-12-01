package io.duzzy.core.provider.corrupted;

import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

public interface LongCorruptedProvider extends Provider<Long> {

    @Override
    default Long corruptedValue(ColumnContext columnContext) {
        return columnContext.random().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
    }
}
