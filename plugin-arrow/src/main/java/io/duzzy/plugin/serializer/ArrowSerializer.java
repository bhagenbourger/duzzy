package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.field.Type;
import io.duzzy.core.serializer.AvroSerializer;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import org.apache.arrow.adapter.avro.AvroToArrow;
import org.apache.arrow.adapter.avro.AvroToArrowConfig;
import org.apache.arrow.adapter.avro.AvroToArrowConfigBuilder;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.DateMilliVector;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.TimeMicroVector;
import org.apache.arrow.vector.TimeMilliVector;
import org.apache.arrow.vector.TimeStampMicroVector;
import org.apache.arrow.vector.TimeStampMilliVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowStreamWriter;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.ArrowSerializer",
    description = "Serialize data to Apache Arrow format",
    module = "io.duzzy.plugin-arrow",
    duzzyType = DuzzyType.SERIALIZER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "name",
            description = "The name of the record"
        ),
        @Parameter(
            name = "namespace",
            description = "The namespace that qualifies the name"
        ),
        @Parameter(
            name = "schema_file",
            description = "The Avro schema file",
            aliases = {"schemaFile", "schema-file"}
        ),
        @Parameter(
            name = "batch_size",
            description = "The size of each batch written to the output stream",
            aliases = {"batchSize", "batch-size"}
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.ConsoleSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.ArrowSerializer"
            name: "arrow_name"
            namespace: "arrow_namespace"
            schema_file: "/path/to/schema.avsc"
            batch_size: 1000
        """
)
public class ArrowSerializer extends AvroSerializer<ArrowStreamWriter> {

  private final int batchSize;
  private int index = 0;
  private BufferAllocator allocator;
  private VectorSchemaRoot schemaRoot;

  @JsonCreator
  public ArrowSerializer(
      @JsonProperty("name")
      String name,
      @JsonProperty("namespace")
      String namespace,
      @JsonProperty("schema_file")
      @JsonAlias({"schemaFile", "schema-file"})
      File schemaFile,
      @JsonProperty("batch_size")
      @JsonAlias({"batchSize", "batch-size"})
      Integer batchSize
  ) {
    super(name, namespace, schemaFile);
    this.batchSize = batchSize == null ? Integer.MAX_VALUE : batchSize;
  }

  @Override
  protected ArrowStreamWriter buildWriter(OutputStream output) throws IOException {
    allocator = new RootAllocator();
    final AvroToArrowConfig config = new AvroToArrowConfigBuilder(allocator).build();
    schemaRoot = VectorSchemaRoot.create(
        AvroToArrow.avroToAvroSchema(getSchema(), config),
        allocator
    );
    final ArrowStreamWriter writer = new ArrowStreamWriter(schemaRoot, null, output);
    writer.start();
    return writer;
  }

  @Override
  protected void serialize(DuzzyRow row, ArrowStreamWriter writer) throws IOException {
    row.cells().forEach(cell -> {
      switch (cell.type()) {
        case Type.INTEGER:
          ((IntVector) schemaRoot.getVector(cell.name())).setSafe(index, (Integer) cell.value());
          break;
        case LONG:
          ((BigIntVector) schemaRoot.getVector(cell.name())).setSafe(index, (Long) cell.value());
          break;
        case UUID:
          ((VarCharVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, cell.value().toString().getBytes(StandardCharsets.UTF_8));
          break;
        case FLOAT:
          ((Float4Vector) schemaRoot.getVector(cell.name())).setSafe(index, (Float) cell.value());
          break;
        case DOUBLE:
          ((Float8Vector) schemaRoot.getVector(cell.name())).setSafe(index, (Double) cell.value());
          break;
        case STRING:
          ((VarCharVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, ((String) cell.value()).getBytes(StandardCharsets.UTF_8));
          break;
        case BOOLEAN:
          ((BitVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, ((Boolean) cell.value()) ? 1 : 0);
          break;
        case INSTANT:
          ((TimeStampMilliVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, ((Instant) cell.value()).toEpochMilli());
          break;
        case LOCAL_DATE:
          ((DateMilliVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, ((LocalDate) cell.value()).toEpochDay());
          break;
        case TIME_MICROS:
          ((TimeMicroVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, (Long) cell.value());
          break;
        case TIME_MILLIS:
          ((TimeMilliVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, (Integer) cell.value());
          break;
        case TIMESTAMP_MICROS:
          ((TimeStampMicroVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, (Long) cell.value());
          break;
        case TIMESTAMP_MILLIS:
          ((TimeStampMilliVector) schemaRoot.getVector(cell.name()))
              .setSafe(index, (Long) cell.value());
          break;
        default:
          throw new UnsupportedOperationException("Unsupported type: " + cell.type());
      }
    });
    index++;
    if (index >= batchSize) {
      writeBatch();
    }
  }

  @Override
  public ArrowSerializer fork(Long threadId) {
    return new ArrowSerializer(name, namespace, schemaFile, batchSize);
  }

  @Override
  public long size(OutputStream output, ArrowStreamWriter writer) {
    return writer == null ? 0 : writer.bytesWritten();
  }

  @Override
  public void close() throws Exception {
    writeBatch();
    getWriter().end();
    schemaRoot.close();
    allocator.close();
    super.close();
  }

  private void writeBatch() throws IOException {
    schemaRoot.setRowCount(index);
    getWriter().writeBatch();
    index = 0;
  }
}
