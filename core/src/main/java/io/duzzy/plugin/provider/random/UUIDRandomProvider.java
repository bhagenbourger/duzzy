package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.duzzy.core.column.ColumnContext;
import io.duzzy.core.provider.Provider;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UUIDRandomProvider implements Provider<UUID> {

    private static final int LEFT_LIMIT = 48; // numeral '0'
    private static final int RIGHT_LIMIT = 122; // letter 'z'
    private static final int LENGTH = 20;

    @JsonCreator
    public UUIDRandomProvider() {
    }

    @Override
    public UUID value(ColumnContext columnContext) {
        final String value = columnContext
                .random()
                .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .limit(LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return UUID.nameUUIDFromBytes(value.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public UUID corruptedValue(ColumnContext columnContext) {
        return value(columnContext);
    }
}
