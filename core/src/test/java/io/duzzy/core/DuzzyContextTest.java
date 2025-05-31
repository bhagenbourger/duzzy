package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.MockSink;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class DuzzyContextTest {

  @Test
  void threadsReturnOneByDefault() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.threads()).isEqualTo(1);
  }

  @Test
  void threadsReturnSpecifiedValue() {
    final int threads = 3;
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null))
        .withThreads(threads)
        .withSink(new MockSink());
    assertThat(duzzyContext.threads()).isEqualTo(threads);
  }

  @Test
  void threadsReturnSinkValue() {
    final int threads = 10;
    final int sinkThread = 5;
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null))
        .withThreads(threads)
        .withSink(new MockSink());
    assertThat(duzzyContext.threads()).isEqualTo(sinkThread);
  }
}
