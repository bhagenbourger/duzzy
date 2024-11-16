package io.duzzy.plugin.sink;

import io.duzzy.plugin.serializer.JsonSerializer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static io.duzzy.tests.Data.getDataOne;
import static io.duzzy.tests.Data.getDataTwo;
import static org.assertj.core.api.Assertions.assertThat;

public class HdfsSinkTest {

    @Test
    void writeJsonFile() throws IOException {
        final String expected = "{\"c1\":1,\"c2\":\"one\"}\n{\"c1\":2,\"c2\":\"two\"}";
        final String filename = "build/hdfs.json";

        final HdfsSink hdfsSink = new HdfsSink(
                new JsonSerializer(),
                null,
                null,
                filename
        );
        hdfsSink.init(List.of());
        hdfsSink.write(getDataOne());
        hdfsSink.write(getDataTwo());
        hdfsSink.close();

        assertThat(Files.readString(Path.of(filename))).isEqualTo(expected);
    }
}
