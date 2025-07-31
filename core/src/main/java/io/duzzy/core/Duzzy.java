package io.duzzy.core;

import io.duzzy.core.config.DuzzyConfig;
import io.duzzy.core.engine.DuzzyEngine;
import io.duzzy.core.engine.DuzzyEngineResult;
import io.duzzy.core.parser.Parser;
import io.duzzy.core.schema.DuzzySchema;
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
  private final Long size;
  private final Long duration;
  private final Integer threads;
  private final String schemaParser;

  public Duzzy(
      File schema,
      File config,
      Long seed,
      Long rows,
      Long size,
      Long duration,
      Integer threads,
      String schemaParser
  ) {
    this.schema = schema;
    this.config = config;
    this.seed = seed;
    this.rows = rows;
    this.size = size;
    this.duration = duration;
    this.threads = threads;
    this.schemaParser = schemaParser;
  }

  public DuzzyResult generate() throws Exception {
    return generate(createDuzzyContext(loadParser(), loadDuzzyConfig()));
  }

  private static DuzzyResult generate(DuzzyContext duzzyContext) throws Exception {
    final long start = Instant.now().toEpochMilli();
    final DuzzyEngineResult duzzyEngineResult = new DuzzyEngine(duzzyContext).processing();
    final long end = Instant.now().toEpochMilli();

    return new DuzzyResult(
        Duration.of(end - start, ChronoUnit.MILLIS),
        Duration.of(duzzyEngineResult.duration(), ChronoUnit.MILLIS),
        duzzyEngineResult.rows(),
        duzzyEngineResult.size(),
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
    final DuzzySchema duzzySchema = schema == null ? null : parser.parse(schema, duzzyConfig);
    return new DuzzyContext(duzzySchema)
        .withSeed(this.seed)
        .withRows(this.rows)
        .withSize(this.size)
        .withDuration(this.duration)
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
