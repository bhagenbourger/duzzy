package io.duzzy.core.engine;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.duzzy.core.DuzzyCell;
import io.duzzy.core.DuzzyLimit;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.provider.constant.StringConstantProvider;
import io.duzzy.plugin.serializer.JsonSerializer;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DuzzyProcessingTest {

  @Mock
  Sink sink;

  @Test
  void shouldRunProcessingWithEmptyRowKey() throws Exception {
    when((JsonSerializer) sink.getSerializer()).thenReturn(new JsonSerializer());
    final DuzzyRow expected = new DuzzyRow(
        Optional.empty(),
        List.of(new DuzzyCell("testField", Type.STRING, "value"))
    );

    final Field field = new Field(
        "testField",
        Type.STRING,
        0f,
        0f,
        List.of(new StringConstantProvider("value"))
    );

    final DuzzySchema duzzySchema = new DuzzySchema(Optional.empty(), List.of(field));
    final DuzzyProcessing processing = new DuzzyProcessing(
        duzzySchema,
        sink,
        1L
    );
    processing.run(
        0,
        1,
        new DuzzyLimit(1L, null, null)
    );

    verify(sink).init(duzzySchema);
    verify(sink).write(expected);
    verify(sink).close();
  }

  @Test
  void shouldRunProcessing() throws Exception {
    when((JsonSerializer) sink.getSerializer()).thenReturn(new JsonSerializer());

    final Field rowKey = new Field(
        "key",
        Type.STRING,
        0f,
        0f,
        List.of(new StringConstantProvider("myKey"))
    );
    final Field field = new Field(
        "myField",
        Type.STRING,
        0f,
        0f,
        List.of(new StringConstantProvider("myValue"))
    );
    final DuzzyRow expected = new DuzzyRow(
        Optional.of("myKey"),
        List.of(new DuzzyCell("myField", Type.STRING, "myValue"))
    );

    final DuzzySchema duzzySchema = new DuzzySchema(Optional.of(rowKey), List.of(field));
    final DuzzyProcessing processing = new DuzzyProcessing(
        duzzySchema,
        sink,
        1L
    );
    processing.run(
        0,
        1,
        new DuzzyLimit(1L, null, null)
    );

    verify(sink).init(duzzySchema);
    verify(sink).write(expected);
    verify(sink).close();
  }
}
