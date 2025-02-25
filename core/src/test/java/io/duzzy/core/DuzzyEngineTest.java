package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.duzzy.core.sink.MockSink;
import java.util.List;
import org.junit.jupiter.api.Test;

public class DuzzyEngineTest {

  @Test
  void shouldComputeBatches() {
    final long total = 100L;
    final int buckets = 6;
    final List<Long> batches = DuzzyEngine.computeBatches(total, buckets);

    assertThat(batches).hasSize(buckets);
    assertThat(batches.stream().reduce(0L, Long::sum)).isEqualTo(total);
    assertThat(batches).containsExactly(16L, 16L, 17L, 17L, 17L, 17L);
  }

  @Test
  void monoThreadProcessing() throws Exception {
    final DuzzyEngine duzzyEngine =
        spy(new DuzzyEngine(new DuzzyContext(null).withSink(new MockSink())));
    duzzyEngine.processing();

    verify(duzzyEngine, times(1)).monoThreadProcessing();
    verify(duzzyEngine, times(0)).multiThreadProcessing();
  }

  @Test
  void multiThreadProcessing() throws Exception {
    final DuzzyEngine duzzyEngine =
        spy(new DuzzyEngine(new DuzzyContext(null).withThreads(2).withSink(new MockSink())));
    duzzyEngine.processing();

    verify(duzzyEngine, times(0)).monoThreadProcessing();
    verify(duzzyEngine, times(1)).multiThreadProcessing();
  }
}
