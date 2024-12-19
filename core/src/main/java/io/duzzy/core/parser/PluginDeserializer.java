package io.duzzy.core.parser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.duzzy.core.Plugin;
import java.io.IOException;

public class PluginDeserializer<T extends Plugin> extends JsonDeserializer<T> {
  private static final String IDENTIFIER = "identifier";

  @Override
  public T deserialize(JsonParser parser, DeserializationContext context) throws IOException {
    JsonNode node = parser.readValueAsTree();
    JsonNode identifier = node.get(IDENTIFIER);
    try {
      Class<?> clazz = Class.forName(identifier.textValue());
      return (T) parser.getCodec().treeToValue(node, clazz);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
