package io.duzzy.plugin.serializer;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.KEY_C1;
import static io.duzzy.tests.Data.KEY_C2;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.createTempFile;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.schema.DuzzySchema;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.provider.increment.IntegerIncrementProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowStreamReader;
import org.junit.jupiter.api.Test;

public class ArrowSerializerTest {

  @Test
  void parsedFromYaml() throws IOException {
    final File serializerFile = getFromResources(getClass(), "serializer/arrow-serializer.yaml");
    final Serializer<?> serializer = YAML_MAPPER.readValue(serializerFile, Serializer.class);


    assertThat(serializer).isInstanceOf(ArrowSerializer.class);
  }

  @Test
  void serializeWithDefaultValues() throws Exception {
    final File file = createTempFile(getClass().getSimpleName());
    final List<Field> fields = List.of(
        new Field(
            KEY_C1,
            Type.INTEGER,
            null,
            null,
            List.of(new IntegerIncrementProvider(null, null))
        ),
        new Field(
            KEY_C2,
            Type.STRING,
            null,
            null,
            List.of(new AlphanumericRandomProvider())
        )
    );

    try (final OutputStream outputStream = new FileOutputStream(file)) {
      final ArrowSerializer arrowSerializer = new ArrowSerializer(null, null, null, null);
      arrowSerializer.init(outputStream, new DuzzySchema(Optional.empty(), fields));
      arrowSerializer.serialize(getDataOne());
      arrowSerializer.serialize(getDataTwo());
      arrowSerializer.close();
    }

    try (
        final BufferAllocator allocator = new RootAllocator(Long.MAX_VALUE);
        final FileInputStream fileInputStream = new FileInputStream(file);
        final ArrowStreamReader reader = new ArrowStreamReader(fileInputStream, allocator);
    ) {
      int index = 0;
      while (reader.loadNextBatch()) {
        try (final VectorSchemaRoot vector = reader.getVectorSchemaRoot()) {
          assertThat(vector.getRowCount()).isEqualTo(2);
          assertThat(vector.contentToTSVString()).isEqualTo("""
              c1\tc2
              1\tone
              2\ttwo
              """);
        }
        index++;
      }
      assertThat(index).isEqualTo(1);
    }
  }
}
