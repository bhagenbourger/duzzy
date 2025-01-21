package io.duzzy.cli;

import io.duzzy.cli.command.PluginCommand;
import io.duzzy.cli.command.RunCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = "duzzy",
    description = "Give me your schema, I'll give you your test data.",
    mixinStandardHelpOptions = true,
    version = App.VERSION,
    subcommands = {RunCommand.class, PluginCommand.class}
)
public class App implements Runnable {

  public static final String VERSION = "0.0.0";

  @Override
  public void run() {
    new CommandLine(new App()).usage(System.out);
  }

  public static void main(String[] args) {
    System.exit(new CommandLine(new App()).execute(args));
  }
}