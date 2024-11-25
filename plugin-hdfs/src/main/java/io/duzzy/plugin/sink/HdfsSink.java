package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.OutputStreamSink;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.io.OutputStream;

public class HdfsSink extends OutputStreamSink {

    @JsonCreator
    public HdfsSink(
            @JsonProperty("serializer") Serializer<?> serializer,
            @JsonProperty("coreSiteFile") String coreSitePath,
            @JsonProperty("hdfsSitePath") String hdfsSitePath,
            @JsonProperty("filename") String filename
    ) throws IOException {
        super(serializer, buildOutputStream(coreSitePath, hdfsSitePath, filename));
    }

    private static OutputStream buildOutputStream(
            String coreSitePath,
            String hdfsSitePath,
            String filename
    ) throws IOException {
        final Configuration configuration = new Configuration();
        if (coreSitePath != null) {
            configuration.addResource(new Path(coreSitePath));
        }
        if (hdfsSitePath != null) {
            configuration.addResource(new Path(hdfsSitePath));
        }
        final FileSystem hdfs = FileSystem.get(configuration);
        final Path file = new Path(filename);
        return hdfs.create(file);
    }
}
