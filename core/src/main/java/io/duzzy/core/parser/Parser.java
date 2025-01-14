package io.duzzy.core.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyContext;
import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.schema.SchemaContext;
import java.io.File;
import java.io.IOException;
import java.util.List;

public interface Parser {

  String NAME = "name";
  String TYPE = "type";

  ObjectMapper YAML_MAPPER = YAMLMapper
      .builder()
      .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
      .build()
      .registerModule(new PluginModule())
      .registerModule(new JavaTimeModule());

  private static Provider<?> getProvider(
      String fieldName,
      Type type,
      DuzzyConfig duzzyConfig,
      Provider<?> defaultValue
  ) {
    return duzzyConfig != null ? duzzyConfig.findProvider(NAME, fieldName)
        .or(() -> duzzyConfig.findProvider(TYPE, type.getName()))
        .orElse(defaultValue) : defaultValue;
  }

  default Field getField(
      String fieldName,
      Type type,
      Float nullRate,
      DuzzyConfig duzzyConfig,
      Provider<?> defaultValue
  ) {
    return new Field(
        fieldName,
        type,
        nullRate,
        null, //TODO corrupted rate
        List.of(getProvider(fieldName, type, duzzyConfig, defaultValue))
    );
  }

  SchemaContext parse(File file, DuzzyConfig duzzyConfig) throws IOException;
}
