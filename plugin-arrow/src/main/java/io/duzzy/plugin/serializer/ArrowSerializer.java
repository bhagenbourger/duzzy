package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.field.Type;
import io.duzzy.core.serializer.AvroSerializer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.apache.arrow.adapter.avro.AvroToArrow;
import org.apache.arrow.adapter.avro.AvroToArrowConfig;
import org.apache.arrow.adapter.avro.AvroToArrowConfigBuilder;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BigIntVector;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.Float4Vector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowStreamWriter;

public class ArrowSerializer extends AvroSerializer<ArrowStreamWriter> {

  private int index = 0;
  private VectorSchemaRoot schemaRoot;

  @JsonCreator
  public ArrowSerializer(
      @JsonProperty("name")
      String name,
      @JsonProperty("namespace")
      String namespace,
      @JsonProperty("schema_file")
      @JsonAlias({"schemaFile", "schema-file"})
      File schemaFile
  ) {
    super(name, namespace, schemaFile);
  }

  @Override
  protected ArrowStreamWriter buildWriter(OutputStream output) throws IOException {
    final BufferAllocator allocator = new RootAllocator();
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
  protected void serialize(DataItems data, ArrowStreamWriter writer) {
    data.items().forEach(item -> {
      switch (item.type()) {
        case Type.INTEGER:
          ((IntVector) schemaRoot.getVector(item.name())).setSafe(index, (Integer) item.value());
          break;
        case LONG:
          ((BigIntVector) schemaRoot.getVector(item.name())).setSafe(index, (Long) item.value());
          break;
        case UUID:
          ((VarCharVector) schemaRoot.getVector(item.name()))
              .setSafe(index, item.value().toString().getBytes(StandardCharsets.UTF_8));
          break;
        case FLOAT:
          ((Float4Vector) schemaRoot.getVector(item.name())).setSafe(index, (Float) item.value());
          break;
        case DOUBLE:
          ((Float8Vector) schemaRoot.getVector(item.name())).setSafe(index, (Double) item.value());
          break;
        case STRING:
          ((VarCharVector) schemaRoot.getVector(item.name()))
              .setSafe(index, ((String) item.value()).getBytes(StandardCharsets.UTF_8));
          break;
        case BOOLEAN:
          ((BitVector) schemaRoot.getVector(item.name()))
              .setSafe(index, ((Boolean) item.value()) ? 1 : 0);
          break;
        case INSTANT:
        case LOCAL_DATE:
        case TIME_MICROS:
        case TIME_MILLIS:
        case TIMESTAMP_MICROS:
        case TIMESTAMP_MILLIS:
        default:
          throw new UnsupportedOperationException("Unsupported type: " + item.type());
      }
    });
    index++;
  }

  @Override
  public ArrowSerializer fork(Long threadId) {
    return new ArrowSerializer(name, namespace, schemaFile);
  }

  @Override
  public long size(OutputStream output, ArrowStreamWriter writer) {
    return writer == null ? 0 : writer.bytesWritten();
  }

  @Override
  public void close() throws Exception {
    getWriter().writeBatch();
    getWriter().end();
    super.close();
  }

  //
//  public static void main(String[] args) throws FileNotFoundException {
//    System.out.println("ArrowSerializer is not yet implemented.");
//    final File file = new File("random_access_file.arrow");
//    final FileOutputStream fileOutputStream = new FileOutputStream(file);
//    final BufferAllocator allocator = new RootAllocator();
//    final AvroToArrowConfig config = new AvroToArrowConfigBuilder(allocator).build();
//    final VectorSchemaRoot schema =
//        VectorSchemaRoot.create(AvroToArrow.avroToAvroSchema(), allocator);
//    final ArrowFileWriter writer =
//        new ArrowFileWriter(schema, /*provider*/ null, fileOutputStream.getChannel());
//  }
}
