package io.duzzy.core.provider;

import io.duzzy.core.Plugin;
import io.duzzy.core.column.ColumnContext;

public interface Provider<T> extends Plugin {

    T value(ColumnContext columnContext);

    T corruptedValue(ColumnContext columnContext);
}


