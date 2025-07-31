package io.duzzy.core.engine;

import static io.duzzy.core.Forkable.MONO_THREAD;
import static java.util.concurrent.Executors.newFixedThreadPool;

import io.duzzy.core.DuzzyContext;
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

  public DuzzyEngineResult processing() throws Exception {
    return duzzyContext.threads() <= MONO_THREAD ? monoThreadProcessing() :
        multiThreadProcessing();
  }

  DuzzyEngineResult monoThreadProcessing() throws Exception {
    return new DuzzyProcessing(
        duzzyContext.duzzySchema(),
        duzzyContext.sink(),
        duzzyContext.seed()
    ).run(0, 1, duzzyContext.duzzyLimit());
  }

  DuzzyEngineResult multiThreadProcessing() throws InterruptedException {
    try (final ExecutorService service = newFixedThreadPool(duzzyContext.threads())) {
      final List<Future<DuzzyEngineResult>> futures = service.invokeAll(computeTasks());
      return futures
          .stream()
          .map(f -> {
            try {
              return f.get();
            } catch (InterruptedException | ExecutionException e) {
              throw new RuntimeException(e);
            }
          })
          .reduce(new DuzzyEngineResult(), DuzzyEngineResult::sum);
    }
  }

  private List<DuzzyThread> computeTasks() {
    final List<DuzzyThread> tasks = new ArrayList<>();
    for (int i = 0; i < duzzyContext.threads(); i++) {
      tasks.add(new DuzzyThread(i, duzzyContext));
    }
    return tasks;
  }
}
