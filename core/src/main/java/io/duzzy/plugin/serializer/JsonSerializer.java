package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import java.io.IOException;
import java.io.OutputStream;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.JsonSerializer",
    description = "Serialize data in JSON",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.SERIALIZER,
    nativeSupport = true,
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.ConsoleSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.JsonSerializer"
        """
)
public class JsonSerializer extends Serializer<SequenceWriter> {

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule());

  @Override
  protected SequenceWriter buildWriter(OutputStream outputStream) throws IOException {
    return MAPPER
        .writer()
        .withRootValueSeparator("\n")
        .writeValues(outputStream);
  }

  @Override
  protected void serialize(DuzzyRow row, SequenceWriter writer) throws IOException {
    writer.write(row.valuesAsMap());
  }

  @Override
  public Boolean hasSchema() {
    return false;
  }

  @Override
  public JsonSerializer fork(Long threadId) {
    return new JsonSerializer();
  }
}
