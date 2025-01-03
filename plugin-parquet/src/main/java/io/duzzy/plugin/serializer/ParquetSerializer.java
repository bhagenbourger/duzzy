package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;

public class ParquetSerializer extends Serializer<ParquetWriter<GenericData.Record>> {

  private static final String DEFAULT_NAME = "name";
  private static final String DEFAULT_NAMESPACE = "namespace";

  private final String name;
  private final String namespace;
  private Schema schema;

  @JsonCreator
  public ParquetSerializer(
      @JsonProperty("name") String name,
      @JsonProperty("namespace") String namespace
  ) {
    this.name = name == null ? DEFAULT_NAME : name;
    this.namespace = namespace == null ? DEFAULT_NAMESPACE : namespace;
  }

  @Override
  protected ParquetWriter<GenericData.Record> buildWriter(OutputStream outputStream)
      throws IOException {
    return AvroParquetWriter
        .<GenericData.Record>builder(new ParquetOutputStreamWriter(outputStream))
        .withSchema(getSchema())
        .build();
  }

  @Override
  protected void serialize(DataItems data, ParquetWriter<GenericData.Record> writer)
      throws IOException {
    writer.write(serializeToRecord(data));
  }

  //TODO: move to a common lib with avro?
  private GenericData.Record serializeToRecord(DataItems data) {
    final GenericRecordBuilder recordBuilder = new GenericRecordBuilder(getSchema());
    data.items().forEach(d -> recordBuilder.set(d.name(), d.value()));
    return recordBuilder.build();
  }

  @Override
  public Boolean hasSchema() {
    return true;
  }

  private Schema getSchema() {
    if (schema == null) {
      schema = buildSchema();
    }
    return schema;
  }

  private Schema buildSchema() {
    //TODO: move to a common lib with avro?
    final SchemaBuilder.FieldAssembler<Schema> fields = SchemaBuilder
        .record(name)
        .namespace(namespace)
        .fields();
    getSchemaContext().columns().forEach(c -> {
      switch (c.columnType()) {
        case INTEGER -> fields.name(c.name()).type().intType().noDefault();
        case LONG -> fields.name(c.name()).type().longType().noDefault();
        case FLOAT -> fields.name(c.name()).type().floatType().noDefault();
        case DOUBLE -> fields.name(c.name()).type().doubleType().noDefault();
        case BOOLEAN -> fields.name(c.name()).type().booleanType().noDefault();
        case STRING -> fields.name(c.name()).type().stringType().noDefault();
        default -> throw new UnsupportedOperationException(
            "Column type is not supported"
        );
      }
    });
    return fields.endRecord();
  }
}
