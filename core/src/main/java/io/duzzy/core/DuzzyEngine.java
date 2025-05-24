package io.duzzy.core;

import static io.duzzy.core.Forkable.MONO_THREAD;
import static java.util.concurrent.Executors.newFixedThreadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DuzzyEngine {

  private final DuzzyContext duzzyContext;

  public DuzzyEngine(DuzzyContext duzzyContext) {
    this.duzzyContext = duzzyContext;
  }

  public long processing() throws Exception {
    return duzzyContext.threads() <= MONO_THREAD ? monoThreadProcessing() : multiThreadProcessing();
  }

  long monoThreadProcessing() throws Exception {
    return new DuzzyProcessing(
        0L,
        duzzyContext.rows(),
        duzzyContext.duzzySchema(),
        duzzyContext.sink(),
        duzzyContext.seed()
    ).run();
  }

  long multiThreadProcessing() throws InterruptedException, ExecutionException {
    long sum = 0;
    try (final ExecutorService executorService = newFixedThreadPool(duzzyContext.threads())) {
      final List<Future<Long>> futures = executorService.invokeAll(computeTasks());
      for (Future<Long> f : futures) {
        sum += f.get();
      }
    }
    return sum;
  }

  private List<DuzzyThread> computeTasks() {
    final List<Long> batches = computeBatches(duzzyContext.rows(), duzzyContext.threads());
    final List<DuzzyThread> tasks = new ArrayList<>();
    long sum = 0L;
    for (Long batch : batches) {
      tasks.add(new DuzzyThread(sum, sum + batch, duzzyContext));
      sum += batch;
    }
    return tasks;
  }

  static List<Long> computeBatches(long total, int buckets) {
    final List<Long> batches = new ArrayList<>();
    while (buckets > 1) {
      final long batch = total / buckets;
      batches.add(batch);
      total -= batch;
      buckets--;
    }
    batches.add(total);
    return batches;
  }
}
