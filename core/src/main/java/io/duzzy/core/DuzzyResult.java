package io.duzzy.core;

import java.time.Duration;

public record DuzzyResult(
    Duration duration,
    Long rows,
    Integer size,
    Long seed
) {
}
