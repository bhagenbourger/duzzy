package io.duzzy.plugin.sink;

import io.duzzy.core.DuzzyContext;
import io.duzzy.core.sink.Sink;
import io.duzzy.plugin.serializer.JsonSerializer;
import org.junit.jupiter.api.Test;

import java.io.*;

import static io.duzzy.core.parser.Parser.YAML_MAPPER;
import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static io.duzzy.tests.Helper.getFromResources;
import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleSinkTest {

    @Test
    void parsedFromYaml() throws IOException {
        final File sinkFile = getFromResources(getClass(), "sink/console-sink.yaml");
        final Sink sink = YAML_MAPPER.readValue(sinkFile, Sink.class);

        assertThat(sink).isInstanceOf(ConsoleSink.class);
    }

    @Test
    void writeJson() throws IOException {
        final String expected = "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}\n{\"c1\":1,\"c2\":\"one\"}";
        final OutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        final ConsoleSink consoleSink = new ConsoleSink(new JsonSerializer());
        consoleSink.init(DuzzyContext.DEFAULT);
        consoleSink.write(getDataOne());
        consoleSink.write(getDataTwo());
        consoleSink.write(getDataOne());
        consoleSink.close();

        assertThat(outputStreamCaptor.toString()).isEqualTo(expected);
    }
}
