package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DataItems;
import io.duzzy.core.Serializer;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;

import java.io.IOException;
import java.io.OutputStream;

@Documentation(
        identifier = "io.duzzy.plugin.serializer.JsonSerializer",
        description = "Serialize data in JSON",
        duzzyType = DuzzyType.SERIALIZER,
        example = """
                ---
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
    protected void write(DataItems data, SequenceWriter writer) throws IOException {
        writer.write(data.toMap());
    }
}
