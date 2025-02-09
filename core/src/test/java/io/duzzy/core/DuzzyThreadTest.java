package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class DuzzyThreadTest {

  @Test
  void call() throws Exception {
    final String expected = """
        {"BooleanConstantField":true,"DoubleConstantField":1.0}
        {"BooleanConstantField":true,"DoubleConstantField":1.0}
        {"BooleanConstantField":true,"DoubleConstantField":1.0}
        """;
    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(byteArrayOutputStream, true, StandardCharsets.UTF_8));
    final DuzzyThread duzzyThread = new DuzzyThread(3L, 6L, new DuzzyContext(null));
    duzzyThread.call();

    assertThat(byteArrayOutputStream.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }
}
