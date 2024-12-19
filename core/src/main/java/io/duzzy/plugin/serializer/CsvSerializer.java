package io.duzzy.plugin.serializer;

import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_COLUMN_SEPARATOR;
import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_LINEFEED;
import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_QUOTE_CHAR;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DataItems;
import io.duzzy.core.serializer.Serializer;
import java.io.IOException;
import java.io.OutputStream;

public class CsvSerializer extends Serializer<SequenceWriter> {

  private static final CsvMapper MAPPER = (CsvMapper) new CsvMapper()
      .registerModule(new JavaTimeModule());

  private final Character quoteChar;
  private final Character columnSeparator;
  private final String lineSeparator;


  @JsonCreator
  public CsvSerializer(
      @JsonProperty("quoteChar") Character quoteChar,
      @JsonProperty("columnSeparator") Character columnSeparator,
      @JsonProperty("lineSeparator") String lineSeparator
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
  protected void serialize(DataItems data, SequenceWriter writer) throws IOException {
    writer.write(data.toValues());
  }

  @Override
  public Boolean hasSchema() {
    return false;
  }
}
