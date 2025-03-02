package io.duzzy.core.parser;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.core.sink.Sink;
import java.io.Serial;

public final class PluginModule extends SimpleModule {

  @Serial
  private static final long serialVersionUID = -7578497161480289908L;

  public static final String NAME = "duzzy-plugin";

  public PluginModule() {
    super(NAME);
    addDeserializer(Provider.class, new PluginDeserializer<>());
    addDeserializer(Serializer.class, new PluginDeserializer<>());
    addDeserializer(Sink.class, new PluginDeserializer<>());
  }
}
