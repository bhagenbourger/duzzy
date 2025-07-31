package io.duzzy.cli.command;

import io.duzzy.cli.AppInfos;
import io.duzzy.cli.output.OutputFormat;
import io.duzzy.core.Duzzy;
import io.duzzy.core.DuzzyResult;
import java.io.File;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "run",
    description = "Generate your test data",
    mixinStandardHelpOptions = true,
    version = AppInfos.VERSION
)
public class RunCommand implements Callable<Integer> {

  private static final Logger LOGGER = LoggerFactory.getLogger(RunCommand.class);
  private static final String PARAM_LABEL_LONG = "Long";

  @Option(
      names = {"-f", "--schema-file"},
      paramLabel = "File",
      description = "Schema source file"
  )
  File schema;

  @Option(
      names = {"-c", "--config-file"},
      paramLabel = "File",
      description = "Config file used to enrich the schema"
  )
  File config;

  @Option(
      names = {"-p", "--schema-parser"},
      paramLabel = "Class",
      description = "Qualified name of the parser class used to parse schema file"
  )
  String parser;

  @Option(
      names = {"-s", "--seed"},
      paramLabel = PARAM_LABEL_LONG,
      description = "Seed used to generate data"
  )
  Long seed;

  @Option(
      names = {"-r", "--rows"},
      paramLabel = PARAM_LABEL_LONG,
      description = "Number of rows to generate (per thread), default is 10"
  )
  Long rows;

  @Option(
      names = {"-z", "--size"},
      paramLabel = PARAM_LABEL_LONG,
      description = "Size of data to generate in bytes (per thread), default is unlimited"
  )
  Long size;

  @Option(
      names = {"-d", "--duration"},
      paramLabel = PARAM_LABEL_LONG,
      description = "Duration of data generation in seconds (per thread), default is unlimited"
  )
  Long duration;

  @Option(
      names = {"-o", "--output"},
      paramLabel = "OutputFormat",
      description = "Output format, supported values: ${COMPLETION-CANDIDATES}"
  )
  OutputFormat outputFormat = OutputFormat.RAW;

  @Option(
      names = {"-t", "--threads"},
      paramLabel = "Integer",
      description = "Number of threads to use to generate data"
  )
  Integer threads = 1;

  @Override
  public Integer call() {
    try {
      final DuzzyResult duzzyResult = new Duzzy(
          schema,
          config,
          seed,
          rows,
          size,
          duration,
          threads,
          parser
      ).generate();
      System.out.println(outputFormat.getDuzzyResultVisitor().format(duzzyResult));
    } catch (Exception e) {
      LOGGER.error("An error occurred while running Duzzy:", e);
      return 1;
    }
    return 0;
  }

}
