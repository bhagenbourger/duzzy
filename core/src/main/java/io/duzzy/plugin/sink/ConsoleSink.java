package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.Serializer;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.sink.OutputStreamSink;

import java.io.*;

@Documentation(
        identifier = "io.duzzy.plugin.sink.ConsoleSink",
        description = "Print output into the console",
        duzzyType = DuzzyType.SINK,
        example = """
                ---
                identifier: "io.duzzy.plugin.sink.ConsoleSink"
                """
)
public class ConsoleSink extends OutputStreamSink {

    @JsonCreator
    public ConsoleSink(@JsonProperty("serializer") Serializer<?> serializer) {
        super(serializer, new ByteArrayOutputStream());
    }

    @Override
    public void close() throws IOException {
        serializer.close();
        System.out.writeBytes(((ByteArrayOutputStream) outputStream).toByteArray());
        //Don't close System.out
    }
}
