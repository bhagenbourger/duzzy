package io.duzzy.plugin.serializer;

import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_COLUMN_SEPARATOR;
import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_LINEFEED;
import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_QUOTE_CHAR;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.serializer.Serializer;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.IOException;
import java.io.OutputStream;

@Documentation(
    identifier = "io.duzzy.plugin.serializer.CsvSerializer",
    description = "Serialize data in CSV",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.SERIALIZER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "quote_char",
            aliases = {"quoteChar", "quote-char"},
            description = "The character used to quote values",
            defaultValue = "'"
        ),
        @Parameter(
            name = "column_separator",
            aliases = {"columnSeparator", "column-separator"},
            description = "The character used to separate columns",
            defaultValue = ";"
        ),
        @Parameter(
            name = "line_separator",
            aliases = {"lineSeparator", "line-separator"},
            description = "The character used to separate lines",
            defaultValue = "|"
        )
    },
    example = """
        ---
        sink:
          identifier: "io.duzzy.plugin.sink.ConsoleSink"
          serializer:
            identifier: "io.duzzy.plugin.serializer.CsvSerializer"
            columnSeparator: ";"
            lineSeparator: "|"
            quoteChar: "'"
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
      @JsonProperty("quote_char")
      @JsonAlias({"quoteChar", "quote-char"})
      Character quoteChar,

      @JsonProperty("column_separator")
      @JsonAlias({"columnSeparator", "column-separator"})
      Character columnSeparator,

      @JsonProperty("line_separator")
      @JsonAlias({"lineSeparator", "line-separator"})
      String lineSeparator
  ) {
    this.quoteChar = quoteChar == null ? Character.valueOf(DEFAULT_QUOTE_CHAR) : quoteChar;
    this.columnSeparator =
        columnSeparator == null ? Character.valueOf(DEFAULT_COLUMN_SEPARATOR) : columnSeparator;
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
  protected void serialize(DuzzyRow row, SequenceWriter writer) throws IOException {
    writer.write(row.valuesAsList());
  }

  @Override
  public Boolean hasSchema() {
    return false;
  }

  @Override
  public CsvSerializer fork(Long threadId) {
    return new CsvSerializer(quoteChar, columnSeparator, lineSeparator);
  }
}
