package io.duzzy.core.parser;

import com.fasterxml.jackson.databind.module.SimpleModule;
import io.duzzy.core.column.Column;
import io.duzzy.core.Serializer;
import io.duzzy.core.sink.Sink;

public class PluginModule extends SimpleModule {
    public static final String NAME = "duzzy-plugin";

    public PluginModule() {
        super(NAME);
        addDeserializer(Column.class, new PluginDeserializer<>());
        addDeserializer(Serializer.class, new PluginDeserializer<>());
        addDeserializer(Sink.class, new PluginDeserializer<>());
    }
}
