package io.duzzy.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import io.duzzy.plugin.provider.constant.DoubleConstantProvider;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    final Field booleanField = new Field(
        "BooleanConstantField",
        Type.BOOLEAN,
        0F,
        0F,
        List.of(new BooleanConstantProvider(true))
    );
    final Field doubleField = new Field(
        "DoubleConstantField",
        Type.DOUBLE,
        0F,
        0F,
        List.of(new DoubleConstantProvider(1.0))
    );
    final DuzzySchema duzzySchema = new DuzzySchema(List.of(booleanField, doubleField));
    final DuzzyThread duzzyThread = new DuzzyThread(3L, 6L, new DuzzyContext(duzzySchema));
    duzzyThread.call();

    assertThat(byteArrayOutputStream.toString(StandardCharsets.UTF_8)).isEqualTo(expected);
  }
}
