package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.documentation.Parameter;
import io.duzzy.core.serializer.AvroSerializer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.avro.generic.GenericData;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.ParquetSerializer",
    description = "Serialize data to Parquet",
    module = "io.duzzy.plugin-parquet",
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
        identifier: "io.duzzy.plugin.serializer.ParquetSerializer"
        name: "parquet"
        namespace: "io.duzzy.plugin.serializer"
        schema_file: "schema.avsc"
        """
)
public class ParquetSerializer extends AvroSerializer<ParquetWriter<GenericData.Record>> {

  @JsonCreator
  public ParquetSerializer(
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
  protected ParquetWriter<GenericData.Record> buildWriter(OutputStream outputStream)
      throws IOException {
    return AvroParquetWriter
        .<GenericData.Record>builder(new ParquetOutputStreamWriter(outputStream))
        .withSchema(getSchema())
        .build();
  }

  @Override
  protected void serialize(DuzzyRow row, ParquetWriter<GenericData.Record> writer)
      throws IOException {
    writer.write(serializeToRecord(row));
  }

  @Override
  public ParquetSerializer fork(Long threadId) {
    return new ParquetSerializer(name, namespace, schemaFile);
  }
}
