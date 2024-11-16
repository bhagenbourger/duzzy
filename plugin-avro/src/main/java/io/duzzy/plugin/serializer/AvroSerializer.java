package io.duzzy.plugin.serializer;

import io.duzzy.core.DataItem;
import io.duzzy.core.DataItems;
import io.duzzy.core.Serializer;
import org.apache.avro.LogicalTypes;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecordBuilder;

import java.io.Closeable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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

    private Schema buildSchema() {
        final SchemaBuilder.FieldAssembler<Schema> fields = SchemaBuilder
                .record(name)
                .namespace(namespace)
                .fields();
        getColumns().forEach(c -> {
            switch (c.getColumnType()) {
                case INTEGER -> fields.name(c.getName()).type().intType().noDefault();
                case INTEGER_NULLABLE -> fields.name(c.getName()).type().nullable().intType().noDefault();
                case LONG -> fields.name(c.getName()).type().longType().noDefault();
                case LONG_NULLABLE -> fields.name(c.getName()).type().nullable().longType().noDefault();
                case FLOAT -> fields.name(c.getName()).type().floatType().noDefault();
                case FLOAT_NULLABLE -> fields.name(c.getName()).type().nullable().floatType().noDefault();
                case DOUBLE -> fields.name(c.getName()).type().doubleType().noDefault();
                case DOUBLE_NULLABLE -> fields.name(c.getName()).type().nullable().doubleType().noDefault();
                case BOOLEAN -> fields.name(c.getName()).type().booleanType().noDefault();
                case BOOLEAN_NULLABLE -> fields.name(c.getName()).type().nullable().booleanType().noDefault();
                case STRING -> fields.name(c.getName()).type().stringType().noDefault();
                case STRING_NULLABLE -> fields.name(c.getName()).type().nullable().stringType().noDefault();
                case UUID -> fields
                        .name(c.getName())
                        .type(LogicalTypes.uuid().addToSchema(Schema.create(Schema.Type.STRING)))
                        .noDefault();
                case DATE -> fields
                        .name(c.getName())
                        .type(LogicalTypes.date().addToSchema(Schema.create(Schema.Type.INT)))
                        .noDefault();
                case TIMESTAMP_MILLIS -> fields
                        .name(c.getName())
                        .type(LogicalTypes.timestampMillis().addToSchema(Schema.create(Schema.Type.LONG)))
                        .noDefault();
                case TIMESTAMP_MICROS -> fields
                        .name(c.getName())
                        .type(LogicalTypes.timestampMicros().addToSchema(Schema.create(Schema.Type.LONG)))
                        .noDefault();
                default -> throw new UnsupportedOperationException(
                        "Column type " + c.getColumnType() + " is not supported"
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
