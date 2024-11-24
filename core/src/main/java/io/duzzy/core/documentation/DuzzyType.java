package io.duzzy.core.documentation;

public enum DuzzyType {
    DUZZY_CONFIG(
            "Duzzy Config",
            "Duzzy Config enables schema enrichment by specifying column provider, sink or serializer"
    ),
    DUZZY_SCHEMA(
            "Duzzy Schema",
            "Duzzy schema is the default input schema in which you can specify whatever you want into the output"
    ),
    PARSER(
            "Parser",
            "Parser parses the input schema and produces a DuzzyContext by combining input schema and duzzy config. " +
                    "DuzzyContext is the contract that describes to the engine what to generate."
    ),
    DUZZY_CONFIG_COLUMN(
            "Duzzy Config Column",
            ""
    ),
    COLUMN(
            "Column",
            "A column representation with a name and a type which manages how the data is generated.  \n" +
                    "Column delegates data generation to the provider."
    ),
    PROVIDER(
            "Provider",
            "Provider generates value or corrupted value. The generated value has always the same type."
    ),
    SINK(
            "Sink",
            "Sink component writes data into the configured output.  \n" +
                    "Before writing the data, sink delegates data serialization to the specified serializer."
    ),
    SERIALIZER(
            "Serializer",
            "Serializer component serializes data into the configured format."
    );

    private final String title;
    private final String description;

    DuzzyType(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
