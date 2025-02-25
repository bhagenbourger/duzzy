package io.duzzy.core;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.SchemaContext;
import io.duzzy.plugin.parser.DuzzySchemaParser;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Duzzy {

  private final File schema;
  private final File config;
  private final Long seed;
  private final Long rows;
  private final Integer threads;
  private final String schemaParser;

  public Duzzy(
      File schema,
      File config,
      Long seed,
      Long rows,
      Integer threads,
      String schemaParser
  ) {
    this.schema = schema;
    this.config = config;
    this.seed = seed;
    this.rows = rows;
    this.threads = threads;
    this.schemaParser = schemaParser;
  }

  public DuzzyResult generate() throws Exception {
    return generate(createDuzzyContext(loadParser(), loadDuzzyConfig()));
  }

  private static DuzzyResult generate(DuzzyContext duzzyContext) throws Exception {
    final Long start = Instant.now().toEpochMilli();
    new DuzzyEngine(duzzyContext).processing();
    final Long end = Instant.now().toEpochMilli();

    return new DuzzyResult(
        Duration.of(end - start, ChronoUnit.MILLIS),
        duzzyContext.rows(),
        duzzyContext.seed()
    );
  }

  private DuzzyConfig loadDuzzyConfig() throws IOException {
    return config != null ? DuzzyConfig.fromFile(config) : null;
  }

  private DuzzyContext createDuzzyContext(
      Parser parser,
      DuzzyConfig duzzyConfig
  ) throws IOException {
    final SchemaContext schemaContext = schema == null ? null : parser.parse(schema, duzzyConfig);
    return new DuzzyContext(schemaContext)
        .withSeed(this.seed)
        .withRows(this.rows)
        .withThreads(this.threads)
        .withSink(duzzyConfig == null ? null : duzzyConfig.sink());
  }

  private Parser loadParser()
      throws InstantiationException,
      IllegalAccessException,
      InvocationTargetException,
      NoSuchMethodException,
      ClassNotFoundException {
    return schemaParser != null && !schemaParser.isEmpty() ? Class
        .forName(schemaParser)
        .asSubclass(Parser.class)
        .getDeclaredConstructor()
        .newInstance() : new DuzzySchemaParser();
  }
}
