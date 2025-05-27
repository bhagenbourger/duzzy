package io.duzzy.core;

import java.util.concurrent.Callable;

public class DuzzyThread implements Callable<Long> {

  private final Long start;
  private final Long end;
  private final DuzzyContext duzzyContext;

  public DuzzyThread(Long start, Long end, DuzzyContext duzzyContext) {
    this.start = start;
    this.end = end;
    this.duzzyContext = duzzyContext;
  }

  @Override
  public Long call() {
    try {
      return new DuzzyProcessing(
          start,
          end,
          duzzyContext.duzzySchema(),
          duzzyContext.sink().fork(Thread.currentThread().threadId()),
          duzzyContext.seed()
      ).run();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
