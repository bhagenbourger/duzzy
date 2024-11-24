package io.duzzy.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.provider.Provider;
import io.duzzy.core.sink.Sink;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Documentation(
        identifier = "io.duzzy.core.config.DuzzyConfig",
        description = "Duzzy Config enables schema enrichment by specifying column provider, sink or serializer",
        duzzyType = DuzzyType.DUZZY_CONFIG,
        parameters = {},
        example = ""
)
public record DuzzyConfig(
        List<DuzzyConfigColumn> columns,
        Sink sink
) {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static DuzzyConfig fromFile(File file) throws IOException {
        return Parser.YAML_MAPPER.readValue(file, DuzzyConfig.class);
    }

    public Optional<Provider<?>> findProvider(
            String key,
            String value
    ) {
        //TODO: what if columns is null?
        return columns()
                .stream()
                .filter(c -> c.querySelectorMatcher(key, value))
                .findFirst()
                .map(DuzzyConfig::buildProvider);
    }

    private static Provider<?> buildProvider(
            DuzzyConfigColumn duzzyConfigColumn
    ) {
        try {
            return OBJECT_MAPPER.convertValue(
                    duzzyConfigColumn.parameters(),
                    Class.forName(duzzyConfigColumn.identifier()).asSubclass(Provider.class)
            );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
