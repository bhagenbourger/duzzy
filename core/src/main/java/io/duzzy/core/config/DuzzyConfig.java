package io.duzzy.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.duzzy.core.field.Field;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.sink.Sink;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public record DuzzyConfig(
    Optional<Field> rowKey,
    List<Enricher> enrichers,
    Sink<?> sink
) {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public DuzzyConfig {
    enrichers = enrichers == null ? List.of() : enrichers;
  }

  public static DuzzyConfig fromFile(File file) throws IOException {
    return Parser.YAML_MAPPER.readValue(file, DuzzyConfig.class);
  }

  public Optional<Provider<?>> findProvider(
      String key,
      String value
  ) {
    return enrichers()
        .stream()
        .filter(c -> c.querySelectorMatcher(key, value))
        .findFirst()
        .map(DuzzyConfig::buildProvider);
  }

  private static Provider<?> buildProvider(
      Enricher enricher
  ) {
    try {
      return OBJECT_MAPPER.convertValue(
          enricher.providerParameters(),
          Class.forName(enricher.providerIdentifier()).asSubclass(Provider.class)
      );
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
