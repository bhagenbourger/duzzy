package io.duzzy.cli.output;

import io.duzzy.core.DuzzyResult;

public class DuzzyResultTxtFormatter implements DuzzyResultFormatter {

  @Override
  public String format(DuzzyResult duzzyResult) {
    return "Duzzy generated " + duzzyResult.rows()
        + " rows in " + duzzyResult.totalDuration()
        + " (processing time: " + duzzyResult.processingDuration() + ")"
        + " which represent " + duzzyResult.size()
        + " bytes of data with seed " + duzzyResult.seed();
  }
}
