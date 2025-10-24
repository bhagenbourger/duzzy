package io.duzzy.core.provider.constant;

import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_COLUMN_SEPARATOR;
import static com.fasterxml.jackson.dataformat.csv.CsvSchema.DEFAULT_LINEFEED;

import com.fasterxml.jackson.core.FormatSchema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public abstract class FileConstantProvider<T> extends ListConstantProvider<T> {

  private static final String COLUMN_SEPARATOR = "column_separator";
  private static final String LINE_SEPARATOR = "line_separator";

  public enum FileFormat {
    CSV(new CsvMapper()),
    JSON(new JsonMapper()),
    XML(new XmlMapper()),
    YAML(new YAMLMapper());

    private final ObjectMapper objectMapper;

    FileFormat(ObjectMapper objectMapper) {
      this.objectMapper = objectMapper;
    }

    public FormatSchema getFormatSchema(Map<String, String> properties) {
      if (this == FileFormat.CSV) {
        final Map<String, String> props = properties == null ? Map.of() : properties;
        return CsvSchema
            .emptySchema()
            .withHeader()
            .withLineSeparator(props.getOrDefault(LINE_SEPARATOR, String.valueOf(DEFAULT_LINEFEED)))
            .withColumnSeparator(
                props
                    .getOrDefault(COLUMN_SEPARATOR, String.valueOf(DEFAULT_COLUMN_SEPARATOR))
                    .charAt(0)
            );
      }
      return null;
    }
  }

  public FileConstantProvider(
      FileFormat fileFormat,
      String urlOrPath,
      String columnName,
      Map<String, String> properties
  ) throws IOException {
    super(
        fileFormat
            .objectMapper
            .registerModule(new JavaTimeModule())
            .readerFor(Map.class)
            .with(fileFormat.getFormatSchema(properties))
            .<Map<String, T>>readValues(inputStream(urlOrPath))
            .readAll()
            .stream()
            .map(r -> r.get(columnName))
            .toList()
    );
  }

  private static InputStream inputStream(String urlOrPath) throws FileNotFoundException {
    return new FileInputStream(urlOrPath);
  }
}
