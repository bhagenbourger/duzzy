package io.duzzy.cli.output;

import io.duzzy.core.DuzzyResult;

public class DuzzyResultTxtFormatter implements DuzzyResultFormatter {

  @Override
  public String format(DuzzyResult duzzyResult) {
    return "Duzzy generated " + duzzyResult.rows()
        + " rows in " + duzzyResult.duration()
        + " which represent " + duzzyResult.size()
        + " bytes of data with seed " + duzzyResult.seed();
  }
}
