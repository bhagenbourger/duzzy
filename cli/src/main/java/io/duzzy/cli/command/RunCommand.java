package io.duzzy.cli.command;

import io.duzzy.cli.App;
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
    version = App.VERSION
)
public class RunCommand implements Callable<Integer> {

  private static final Logger logger = LoggerFactory.getLogger(RunCommand.class);

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
      paramLabel = "Long",
      description = "Seed used to generate data"
  )
  Long seed;

  @Option(
      names = {"-r", "--rows"},
      paramLabel = "Long",
      description = "Number of rows to generate"
  )
  Long rows;

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
          threads,
          parser
      ).generate();
      System.out.println(outputFormat.getDuzzyResultVisitor().format(duzzyResult));
    } catch (Exception e) {
      logger.error("An error occurred while running Duzzy:", e);
      return 1;
    }
    return 0;
  }

}
