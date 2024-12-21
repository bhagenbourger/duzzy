package io.duzzy.cli;

import io.duzzy.core.DuzzyResult;

public class DuzzyResultRawFormatter implements DuzzyResultFormatter {

  @Override
  public String format(DuzzyResult duzzyResult) {
    return duzzyResult.toString();
  }
}
