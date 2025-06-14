package io.duzzy.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.duzzy.core.field.Field;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.sink.Sink;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Documentation(
    identifier = "io.duzzy.core.config.DuzzyConfig",
    description = "Duzzy Config enables schema enrichment by specifying column provider, "
        + "sink or serializer",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.DUZZY_CONFIG,
    nativeSupport = true,
    example = """
        ---
        enrichers:
          - query_selector: "name=city"
            provider_identifier: "io.duzzy.plugin.provider.random.AlphanumericRandomProvider"
            provider_parameters:
              min_length: 3
              max_length: 20
          - query_selector: "type=INTEGER"
            provider_identifier: "io.duzzy.plugin.provider.random.IntegerRandomProvider"
            provider_parameters:
              min: 1
              max: 9999
        sink:
          identifier: "io.duzzy.plugin.sink.LocalFileSink"
          filename: "/tmp/example.xml"
          serializer:
            identifier: "io.duzzy.plugin.serializer.XmlSerializer"
            root_tag: myRootExample
            row_tag: myRowExample
        """
)
public record DuzzyConfig(
    Optional<Field> rowKey,
    List<Enricher> enrichers,
    Sink sink
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
