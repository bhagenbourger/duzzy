package io.duzzy.plugin.serializer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DataItems;
import io.duzzy.core.Serializer;
import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;

import java.io.IOException;
import java.io.OutputStream;

import static com.fasterxml.jackson.dataformat.csv.CsvSchema.*;

@Documentation(
        identifier = "io.duzzy.plugin.serializer.CsvSerializer",
        description = "Serialize data in CSV",
        duzzyType = DuzzyType.SERIALIZER,
        example = """
                ---
                identifier: "io.duzzy.plugin.serializer.CsvSerializer"
                columnSeparator: ";"
                lineSeparator: "|"
                """
)
public class CsvSerializer extends Serializer<SequenceWriter> {

    private static final CsvMapper MAPPER = (CsvMapper) new CsvMapper()
            .registerModule(new JavaTimeModule());

    private final Character quoteChar;
    private final Character columnSeparator;
    private final String lineSeparator;


    @JsonCreator
    public CsvSerializer(
            @JsonProperty("quoteChar")
            @JsonAlias({"quote_char", "quote-char"})
            Character quoteChar,
            @JsonProperty("columnSeparator")
            @JsonAlias({"column_separator", "column-separator"})
            Character columnSeparator,
            @JsonProperty("lineSeparator")
            @JsonAlias({"line_separator", "line-separator"})
            String lineSeparator
    ) {
        this.quoteChar = quoteChar == null ? DEFAULT_QUOTE_CHAR : quoteChar;
        this.columnSeparator = columnSeparator == null ? DEFAULT_COLUMN_SEPARATOR : columnSeparator;
        this.lineSeparator = lineSeparator == null ? String.valueOf(DEFAULT_LINEFEED) : lineSeparator;
    }

    @Override
    protected SequenceWriter buildWriter(OutputStream outputStream) throws IOException {
        final CsvSchema schema = CsvSchema.emptySchema()
                .withQuoteChar(quoteChar)
                .withColumnSeparator(columnSeparator)
                .withLineSeparator(lineSeparator);
        return MAPPER
                .writer(schema)
                .writeValues(outputStream);
    }

    @Override
    protected void write(DataItems data, SequenceWriter writer) throws IOException {
        writer.write(data.toValues());
    }
}
