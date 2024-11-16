package io.duzzy.cli;

import io.duzzy.core.DuzzyResult;

public interface DuzzyResultFormatter {
    String format(DuzzyResult duzzyResult);
}
