package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.constant.FileConstantProvider;
import io.duzzy.core.provider.corrupted.StringCorruptedProvider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.documentation.Parameter;
import java.io.IOException;
import java.util.Map;

@Documentation(
    identifier = "io.duzzy.plugin.provider.constant.StringFileConstantProvider",
    description = "Generate string values from a file",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    parameters = {
        @Parameter(
            name = "url_or_path",
            aliases = {"urlOrPath", "url-or-path"},
            description = "The URL or path to the file"
        ),
        @Parameter(
            name = "file_format",
            aliases = {"fileFormat", "file-format"},
            description = "The format of the file, available options are: CSV, JSON, XML, YAML. "
                + "If CSV is used, header is required, "
                + "additional properties can be provided to customize the format."
        ),
        @Parameter(
            name = "column_name",
            aliases = {"columnName", "column-name"},
            description = "The name of the column to read the values from"
        ),
        @Parameter(
            name = "properties",
            description = "Additional properties for the file format, only used for CSV files. "
                + "Available options are: " 
                + "column_separator (default: ,), " 
                + "line_separator (default: \\n)"
        )
    },
    example = """
        ---
        fields:
          - name: first_name
            type: STRING
            provider:
              identifier: "io.duzzy.plugin.provider.constant.StringFileConstantProvider"
              url_or_path: "/path/to/file/data.csv"
              file_format: "CSV"
              column_name: "first_name"
              properties:
                column_separator: ";"
                line_separator: "\\n"
        """
)
public class StringFileConstantProvider extends FileConstantProvider<String> {

  @JsonCreator
  public StringFileConstantProvider(
      @JsonProperty("url_or_path")
      @JsonAlias({"urlOrPath", "url-or-path"})
      String urlOrPath,
      @JsonProperty("file_format")
      @JsonAlias({"fileFormat", "file-format"})
      FileFormat fileFormat,
      @JsonProperty("column_name")
      @JsonAlias({"columnName", "column-name"})
      String columnName,
      @JsonProperty("properties")
      Map<String, String> properties
  ) throws IOException {
    super(fileFormat, urlOrPath, columnName, properties);
  }

  @Override
  public String corruptedValue(FieldContext fieldContext) {
    return StringCorruptedProvider.corruptedValue(fieldContext, 255);
  }
}
