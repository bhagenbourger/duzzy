package io.duzzy.plugin.sink;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class LocalFileSink extends Sink {

  @JsonCreator
  public LocalFileSink(
      @JsonProperty("serializer") Serializer<?> serializer,
      @JsonProperty("filename") String filename
  ) throws FileNotFoundException {
    super(serializer, new FileOutputStream(filename));
  }
}
