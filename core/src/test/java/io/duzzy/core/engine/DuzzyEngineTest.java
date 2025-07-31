package io.duzzy.core.engine;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.sink.MockSink;
import org.junit.jupiter.api.Test;

public class DuzzyEngineTest {

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
