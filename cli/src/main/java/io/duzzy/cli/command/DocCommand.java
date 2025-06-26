package io.duzzy.cli.command;

import io.duzzy.cli.AppInfos;
import io.duzzy.cli.documentation.DocMarkdownFormatter;
import io.duzzy.documentation.DuzzyDoc;
import io.duzzy.documentation.DuzzyType;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "doc",
    description = "Print Duzzy documentation",
    mixinStandardHelpOptions = true,
    version = AppInfos.VERSION
)
public class DocCommand implements Callable<Integer> {

  private static final Logger LOGGER = LoggerFactory.getLogger(DocCommand.class);

  @Option(
      names = {"-t", "--duzzy-type"},
      paramLabel = "DuzzyType",
      description = "Filter documentation by duzzy type"
  )
  DuzzyType duzzyType;

  @Option(
      names = {"-i", "--identifier"},
      paramLabel = "String",
      description = "Filter documentation by identifier name (contains this string)"
  )
  String identifier;

  @Option(
      names = {"-m", "--module"},
      paramLabel = "String",
      description = "Filter documentation by module name (contains this string)"
  )
  String module;

  @Option(
      names = {"-n", "--native"},
      paramLabel = "Boolean",
      description = "Filter documentation by native components only"
  )
  boolean nativeOnly = false;

  @Override
  public Integer call() {
    try {
      System.out.println(
          DocMarkdownFormatter.format(DuzzyDoc.generate(duzzyType, identifier, module, nativeOnly))
      );
    } catch (Exception e) {
      LOGGER.error("An error occurred while generating Duzzy documentation:", e);
      return 1;
    }
    return 0;
  }
}
