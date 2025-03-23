package io.duzzy.cli.output;

import io.duzzy.core.DuzzyResult;

public interface DuzzyResultFormatter {
  String format(DuzzyResult duzzyResult);
}
