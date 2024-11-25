package io.duzzy.plugin.schema;

import io.duzzy.core.schema.InputSchema;
import org.apache.avro.Schema;

public record AvroInputSchema(Schema schema) implements InputSchema {

}
