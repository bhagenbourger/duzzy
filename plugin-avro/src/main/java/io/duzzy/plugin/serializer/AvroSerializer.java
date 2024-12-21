package io.duzzy.plugin.serializer;

import io.duzzy.core.DataItem;
import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.plugin.schema.AvroInputSchema;
import java.io.Closeable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

abstract class AvroSerializer<W extends Closeable> extends Serializer<W> {

  private static final String DEFAULT_NAME = "name";
  private static final String DEFAULT_NAMESPACE = "namespace";

  private final String name;
  private final String namespace;
  private Schema schema;

  public AvroSerializer(String name, String namespace) {
    this.name = name == null ? DEFAULT_NAME : name;
    this.namespace = namespace == null ? DEFAULT_NAMESPACE : namespace;
  }

  @Override
  public Boolean hasSchema() {
    return true;
  }

  private Schema buildSchema() {
    if (getSchemaContext().inputSchema() instanceof AvroInputSchema) {
      return ((AvroInputSchema) getSchemaContext().inputSchema()).schema();
    }

    final SchemaBuilder.FieldAssembler<Schema> fields = SchemaBuilder
        .record(name)
        .namespace(namespace)
        .fields();
    getSchemaContext().columns().forEach(c -> {
      switch (c.columnType()) {
        case INTEGER -> fields.name(c.name()).type().intType().noDefault();
        case INTEGER_NULLABLE -> fields.name(c.name()).type().nullable().intType().noDefault();
        case LONG -> fields.name(c.name()).type().longType().noDefault();
        case LONG_NULLABLE -> fields.name(c.name()).type().nullable().longType().noDefault();
        case FLOAT -> fields.name(c.name()).type().floatType().noDefault();
        case FLOAT_NULLABLE -> fields.name(c.name()).type().nullable().floatType().noDefault();
        case DOUBLE -> fields.name(c.name()).type().doubleType().noDefault();
        case DOUBLE_NULLABLE -> fields.name(c.name()).type().nullable().doubleType().noDefault();
        case BOOLEAN -> fields.name(c.name()).type().booleanType().noDefault();
        case BOOLEAN_NULLABLE -> fields.name(c.name()).type().nullable().booleanType().noDefault();
        case STRING -> fields.name(c.name()).type().stringType().noDefault();
        case STRING_NULLABLE -> fields.name(c.name()).type().nullable().stringType().noDefault();
        case UUID -> fields
            .name(c.name())
            .type(LogicalTypes.uuid().addToSchema(Schema.create(Schema.Type.STRING)))
            .noDefault();
        case DATE -> fields
            .name(c.name())
            .type(LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)))
            .noDefault();
        case TIMESTAMP_MILLIS -> fields
            .name(c.name())
            .type(LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)))
            .noDefault();
        case TIMESTAMP_MICROS -> fields
            .name(c.name())
            .type(LogicalTypes.timestampMicros().addToSchema(Schema.create(Schema.Type.LONG)))
            .noDefault();
        default -> throw new UnsupportedOperationException(
            "Column type " + c.columnType() + " is not supported"
        );
      }
    });
    return fields.endRecord();
  }

  protected GenericData.Record serialize(DataItems data) {
    final GenericRecordBuilder recordBuilder = new GenericRecordBuilder(getSchema());
    data.items().forEach(d -> recordBuilder.set(d.name(), toPrimitiveType(d)));
    return recordBuilder.build();
  }

  protected Schema getSchema() {
    if (schema == null) {
      schema = buildSchema();
    }
    return schema;
  }

  private Object toPrimitiveType(DataItem item) {
    return switch (item.type()) {
      case UUID -> item.value().toString();
      case DATE -> ((LocalDate) item.value()).toEpochDay();
      case TIMESTAMP_MILLIS -> ((Instant) item.value()).toEpochMilli();
      case TIMESTAMP_MICROS -> ChronoUnit.MICROS.between(Instant.EPOCH, ((Instant) item.value()));
      default -> item.value();
    };
  }
}
