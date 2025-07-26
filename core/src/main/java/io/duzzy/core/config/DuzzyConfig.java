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
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.text.StringSubstitutor;

@Documentation(
    identifier = "io.duzzy.core.config.DuzzyConfig",
    description = "Duzzy Config enables schema enrichment by specifying " 
        + "custom row key, column provider, sink or serializer. "
        + "You can use environment variables or system properties in the config.",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.DUZZY_CONFIG,
    nativeSupport = true,
    example = """
        ---
        row_key:
          name: key
          type: STRING
          null_rate: 0
          corrupted_rate: 0
          providers:
            - identifier: "io.duzzy.plugin.provider.random.AlphanumericRandomProvider"
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
          - query_selector: "name=user"
            provider_identifier: "io.duzzy.plugin.provider.constant.StringConstantProvider"
            provider_parameters:
              value: ${USER}
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
    final String content = Files.readString(file.toPath());
    return Parser.YAML_MAPPER.readValue(substitute(content), DuzzyConfig.class);
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

  private static String substitute(String content) {
    final Map<String, String> values = systemPropertiesToMap();
    values.putAll(System.getenv());
    final StringSubstitutor substitutor = new StringSubstitutor(values);
    return substitutor.replace(content);
  }

  private static Map<String, String> systemPropertiesToMap() {
    return System.getProperties().entrySet().stream().collect(
        Collectors.toMap(
            e -> String.valueOf(e.getKey()),
            e -> String.valueOf(e.getValue()),
            (prev, next) -> next, HashMap::new
        ));
  }
}
