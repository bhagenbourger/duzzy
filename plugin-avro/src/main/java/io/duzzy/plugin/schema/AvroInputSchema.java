package io.duzzy.plugin.schema;

import io.duzzy.core.schema.InputSchema;
import org.apache.avro.Schema;

public class AvroInputSchema implements InputSchema {

    private final Schema schema;

    public AvroInputSchema(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }
}
