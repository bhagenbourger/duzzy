package io.duzzy.core;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public record DuzzyResult(
    Optional<List<String>> errors,
    Duration duration,
    Long rows,
    Long seed
) {
}
