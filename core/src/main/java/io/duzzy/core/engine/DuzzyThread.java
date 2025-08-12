package io.duzzy.core.engine;

import io.duzzy.core.DuzzyContext;
import java.util.concurrent.Callable;

public class DuzzyThread implements Callable<DuzzyEngineResult> {

  private final int start;
  private final DuzzyContext duzzyContext;

  public DuzzyThread(int start, DuzzyContext duzzyContext) {
    this.start = start;
    this.duzzyContext = duzzyContext;
  }

  @Override
  public DuzzyEngineResult call() {
    try {
      return new DuzzyProcessing(
          duzzyContext.duzzySchema(),
          duzzyContext.sink().fork(start),
          duzzyContext.seed()
      ).run(start, duzzyContext.threads(), duzzyContext.duzzyLimit());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
