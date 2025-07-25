package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.AvroSerializer;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.generic.GenericData.Record;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.io.EncoderFactory;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.AvroSchemalessSerializer",
    description = "Serialize data to Avro, schema is not written with data",
    module = "io.duzzy.plugin-avro",
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
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.ConsoleSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.AvroSchemalessSerializer"
            name: "avro_name"
            namespace: "avro_namespace"
        """
)
public class AvroSchemalessSerializer extends AvroSerializer<BinaryEncoderCloseable> {

  @JsonCreator
  public AvroSchemalessSerializer(
      @JsonProperty("name") String name,
      @JsonProperty("namespace") String namespace,
      @JsonProperty("schema_file")
      @JsonAlias({"schemaFile", "schema-file"})
      File schemaFile
  ) {
    super(name, namespace, schemaFile);
  }

  @Override
  protected BinaryEncoderCloseable buildWriter(OutputStream outputStream) {
    return new BinaryEncoderCloseable(EncoderFactory.get().directBinaryEncoder(outputStream, null));
  }

  @Override
  protected void serialize(DuzzyRow row, BinaryEncoderCloseable writer) throws IOException {
    new GenericDatumWriter<Record>(getSchema()).write(
        serializeToRecord(row),
        writer.binaryEncoder()
    );
  }

  @Override
  public AvroSchemalessSerializer fork(Long threadId) {
    return new AvroSchemalessSerializer(name, namespace, schemaFile);
  }
}
