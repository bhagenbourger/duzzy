package io.duzzy.core.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzySchema;
import io.duzzy.core.config.DuzzyConfig;

import java.io.File;
import java.io.IOException;

public interface Parser {
    ObjectMapper YAML_MAPPER = YAMLMapper
            .builder()
            .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build()
            .registerModule(new PluginModule())
            .registerModule(new JavaTimeModule());

    DuzzySchema parse(File file, DuzzyConfig duzzyConfig) throws IOException;
}
