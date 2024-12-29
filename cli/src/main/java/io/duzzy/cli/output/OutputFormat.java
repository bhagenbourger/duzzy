package io.duzzy.cli.output;

public enum OutputFormat {
  RAW(new DuzzyResultRawFormatter()),
  TXT(new DuzzyResultTxtFormatter()),
  JSON(new DuzzyResultJsonFormatter()),
  XML(new DuzzyResultXmlFormatter()),
  YAML(new DuzzyResultYamlFormatter());

  private final DuzzyResultFormatter duzzyResultFormatter;

  OutputFormat(DuzzyResultFormatter duzzyResultFormatter) {
    this.duzzyResultFormatter = duzzyResultFormatter;
  }

  public DuzzyResultFormatter getDuzzyResultVisitor() {
    return duzzyResultFormatter;
  }
}
