package io.duzzy.core.parser;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;

public class PluginModule extends SimpleModule {
  public static final String NAME = "duzzy-plugin";

  public PluginModule() {
    super(NAME);
    addDeserializer(Provider.class, new PluginDeserializer<>());
    addDeserializer(Serializer.class, new PluginDeserializer<>());
    addDeserializer(Sink.class, new PluginDeserializer<>());
  }
}
