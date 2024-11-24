package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DataItems;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.serializer.AvroSerializer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.AvroWithSchemaSerializer",
    description = "Serialize data to Avro, schema is written with data",
    duzzyType = DuzzyType.SERIALIZER,
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
        identifier: "io.duzzy.plugin.serializer.AvroWithSchemaSerializer"
        name: "avro-with-schema"
        namespace: "io.duzzy.plugin.serializer"
        schema_file: "schema.avsc"
        """
)
public class AvroWithSchemaSerializer extends AvroSerializer<DataFileWriter<GenericData.Record>> {

  @JsonCreator
  public AvroWithSchemaSerializer(
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
  protected DataFileWriter<GenericData.Record> buildWriter(OutputStream outputStream)
      throws IOException {
    return new DataFileWriter<GenericData.Record>(
        new GenericDatumWriter<>()).create(getSchema(),
        outputStream
    );
  }

  @Override
  protected void serialize(DataItems data, DataFileWriter<GenericData.Record> writer)
      throws IOException {
    writer.append(serializeToRecord(data));
  }

  @Override
  public AvroWithSchemaSerializer fork(Long threadId) {
    return new AvroWithSchemaSerializer(name, namespace, schemaFile);
  }
}
