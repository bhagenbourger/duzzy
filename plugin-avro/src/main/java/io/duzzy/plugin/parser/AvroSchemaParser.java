package io.duzzy.plugin.parser;

import io.duzzy.core.DuzzySchema;
import io.duzzy.core.column.Column;
import io.duzzy.core.column.ColumnType;
import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.plugin.column.constant.StringListConstantColumn;
import io.duzzy.plugin.column.random.*;
import org.apache.avro.LogicalType;
import org.apache.avro.Schema;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static io.duzzy.core.column.ColumnType.*;
import static java.util.stream.Collectors.toUnmodifiableList;

public class AvroSchemaParser implements Parser {

    private static final String NAME = "name";
    private static final String TYPE = "type";

    @Override
    public DuzzySchema parse(File file, DuzzyConfig duzzyConfig) throws IOException {
        final Schema avroSchema = new Schema.Parser().parse(file);
        if (avroSchema.hasFields()) {
            final List<Column<?>> columns = avroSchema
                    .getFields()
                    .stream()
                    .map(f -> AvroSchemaParser.parse(f, duzzyConfig))
                    .collect(toUnmodifiableList());
            return new DuzzySchema(columns, null, null, null);
        }
        return new DuzzySchema(null, null, null, null);
    }

    private static Column<?> parse(Schema.Field field, DuzzyConfig duzzyConfig) {
        if (field.schema().getLogicalType() != null) {
            return parse(field.name(), field.schema().getLogicalType(), duzzyConfig);
        } else if (field.schema().isUnion()) {
            if (field.schema().isNullable()) {
                return parse(
                        field.name(),
                        field
                                .schema()
                                .getTypes()
                                .stream()
                                .filter(t -> t.getType() != Schema.Type.NULL)
                                .toList()
                                .getFirst()
                                .getType(),
                        field.schema(),
                        duzzyConfig
                );
            } else {
                throw new RuntimeException(
                        "Field name '" + field.name() + "' - union type not nullable is not supported"
                );
            }
        } else {
            return parse(field.name(), field.schema().getType(), field.schema(), duzzyConfig);
        }
    }

    private static Column<?> parse(
            String fieldName,
            LogicalType fieldLogicalType,
            DuzzyConfig duzzyConfig
    ) {
        return switch (fieldLogicalType.getName()) {
            case "uuid" -> getColumn(
                    fieldName,
                    ColumnType.UUID,
                    duzzyConfig,
                    new UUIDRandomColumn(fieldName, null)
            );
            case "date" -> getColumn(
                    fieldName,
                    ColumnType.DATE,
                    duzzyConfig,
                    new LocalDateRandomColumn(fieldName, null, null, null)
            );
            case "decimal" -> throw new RuntimeException(
                    "Field name '" + fieldName + "' - avro logical type 'decimal' is not supported"
            );
            case "time-millis" -> throw new RuntimeException(
                    "Field name '$fieldName' - avro logical type 'time-millis' is not supported"
            );
            case "time-micros" -> throw new RuntimeException(
                    "Field name '$fieldName' - avro logical type 'time-micros' is not supported"
            );
            case "timestamp-millis" -> getColumn(
                    fieldName,
                    ColumnType.TIMESTAMP_MILLIS,
                    duzzyConfig,
                    new InstantRandomColumn(fieldName, TIMESTAMP_MILLIS)
            );
            case "timestamp-micros" -> getColumn(
                    fieldName,
                    ColumnType.TIMESTAMP_MICROS,
                    duzzyConfig,
                    new InstantRandomColumn(fieldName, TIMESTAMP_MICROS)
            );
            case "local-timestamp-millis" -> throw new RuntimeException(
                    "Field name '$fieldName' - avro logical type 'local-timestamp-millis' is not supported"
            );
            case "local-timestamp-micros" -> throw new RuntimeException(
                    "Field name '$fieldName' - avro logical type 'local-timestamp-micros' is not supported"
            );
            case null, default -> new AlphanumericRandomColumn(fieldName, null);
        };
    }

    private static Column<?> parse(
            String fieldName,
            Schema.Type fieldType,
            Schema schema,
            DuzzyConfig duzzyConfig
    ) {
        return switch (fieldType) {
            case Schema.Type.STRING -> getColumn(
                    fieldName,
                    schema.isNullable() ? STRING_NULLABLE : STRING,
                    duzzyConfig,
                    new AlphanumericRandomColumn(
                            fieldName,
                            schema.isNullable() ? STRING_NULLABLE : STRING
                    )
            );
            case Schema.Type.INT -> getColumn(
                    fieldName,
                    schema.isNullable() ? INTEGER_NULLABLE : INTEGER,
                    duzzyConfig,
                    new IntegerRandomColumn(
                            fieldName,
                            schema.isNullable() ? INTEGER_NULLABLE : INTEGER
                    )
            );
            case Schema.Type.LONG -> getColumn(
                    fieldName,
                    schema.isNullable() ? LONG_NULLABLE : LONG,
                    duzzyConfig,
                    new LongRandomColumn(
                            fieldName,
                            schema.isNullable() ? LONG_NULLABLE : LONG
                    )
            );
            case Schema.Type.FLOAT -> getColumn(
                    fieldName,
                    schema.isNullable() ? FLOAT_NULLABLE : FLOAT,
                    duzzyConfig,
                    new FloatRandomColumn(
                            fieldName,
                            schema.isNullable() ? FLOAT_NULLABLE : FLOAT
                    )
            );
            case Schema.Type.DOUBLE -> getColumn(
                    fieldName,
                    schema.isNullable() ? DOUBLE_NULLABLE : DOUBLE,
                    duzzyConfig,
                    new DoubleRandomColumn(
                            fieldName,
                            schema.isNullable() ? DOUBLE_NULLABLE : DOUBLE
                    )
            );
            case Schema.Type.BOOLEAN -> getColumn(
                    fieldName,
                    schema.isNullable() ? ColumnType.BOOLEAN_NULLABLE : ColumnType.BOOLEAN,
                    duzzyConfig,
                    new BooleanRandomColumn(
                            fieldName,
                            schema.isNullable() ? ColumnType.BOOLEAN_NULLABLE : ColumnType.BOOLEAN
                    )
            );
            case Schema.Type.ENUM ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'ENUM' is not supported");
            case Schema.Type.UNION ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'UNION' is not supported");
            case Schema.Type.RECORD ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'RECORD' is not supported");
            case Schema.Type.ARRAY ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'ARRAY' is not supported");
            case Schema.Type.MAP ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'MAP' is not supported");
            case Schema.Type.FIXED ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'FIXED' is not supported");
            case Schema.Type.BYTES ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'BYTES' is not supported");
            case Schema.Type.NULL ->
                    throw new RuntimeException("Field name '" + fieldName + "' - avro type 'NULL' is not supported");
        };
    }

    private static Column<?> getColumn(
            String columnName,
            ColumnType columnType,
            DuzzyConfig duzzyConfig,
            Column<?> defaultValue) {
        return duzzyConfig != null ?
                duzzyConfig.findColumn(columnName, columnType, NAME, columnName)
                        .or(() -> duzzyConfig.findColumn(columnName, columnType, TYPE, columnType.getName()))
                        .orElse(defaultValue) : defaultValue;
    }
}
