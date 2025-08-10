package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.MockSink;
import io.duzzy.plugin.sink.ConsoleSink;
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

  @Test
  void sinkReturnsConsoleSinkByDefault() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.sink()).isInstanceOf(ConsoleSink.class);
  }

  @Test
  void seedIsRandomlyGeneratedByDefault() {
    final DuzzyContext duzzyContext1 = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    final DuzzyContext duzzyContext2 = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext1.seed()).isNotNull();
    assertThat(duzzyContext2.seed()).isNotNull();
    // Not guaranteed to be different, but highly likely
    assertThat(duzzyContext1.seed()).isNotEqualTo(duzzyContext2.seed());
  }

  @Test
  void duzzyLimitReturnsDefaultWhenNotProvided() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.duzzyLimit()).isNotNull();
  }

  @Test
  void withSeedReturnsSameInstanceWhenSeedIsNull() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.withSeed(null)).isSameAs(duzzyContext);
  }

  @Test
  void withRowsReturnsSameInstanceWhenRowsIsNull() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.withRows(null)).isSameAs(duzzyContext);
  }

  @Test
  void withRowsReturnsDuzzyContextWithRow() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    final DuzzyContext updatedContext = duzzyContext.withRows(100L);
    assertThat(updatedContext).isNotSameAs(duzzyContext);
    assertThat(updatedContext.duzzyLimit().rows()).isEqualTo(100L);
  }

  @Test
  void withSizeReturnsSameInstanceWhenSizeIsNull() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.withSize(null)).isSameAs(duzzyContext);
  }

  @Test
  void withSizeReturnsDuzzyContextWithSize() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    final DuzzyContext updatedContext = duzzyContext.withSize(1000L);
    assertThat(updatedContext).isNotSameAs(duzzyContext);
    assertThat(updatedContext.duzzyLimit().size()).isEqualTo(1000L);
  }

  @Test
  void withDurationReturnsSameInstanceWhenDurationIsNull() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.withDuration(null)).isSameAs(duzzyContext);
  }

  @Test
  void withDurationReturnsDuzzyContextWithDuration() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    final DuzzyContext updatedContext = duzzyContext.withDuration(1000L);
    assertThat(updatedContext).isNotSameAs(duzzyContext);
    assertThat(updatedContext.duzzyLimit().duration()).isEqualTo(1000L);
  }

  @Test
  void withSinkReturnsSameInstanceWhenSinkIsNull() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.withSink(null)).isSameAs(duzzyContext);
  }

  @Test
  void withThreadsReturnsSameInstanceWhenThreadsIsNull() {
    final DuzzyContext duzzyContext = new DuzzyContext(new DuzzySchema(Optional.empty(), null));
    assertThat(duzzyContext.withThreads(null)).isSameAs(duzzyContext);
  }
}
