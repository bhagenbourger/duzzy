package io.duzzy.cli;

import io.duzzy.cli.command.DocCommand;
import io.duzzy.cli.command.PluginCommand;
import io.duzzy.cli.command.RunCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(
    name = AppInfos.NAME,
    description = AppInfos.DESCRIPTION,
    mixinStandardHelpOptions = true,
    version = AppInfos.VERSION,
    subcommands = {RunCommand.class, PluginCommand.class, DocCommand.class}
)
public class AppJvm implements Runnable {

  @Override
  public void run() {
    new CommandLine(new AppJvm()).usage(System.out);
  }

  public static void main(String[] args) {
    System.exit(new CommandLine(new AppJvm()).execute(args));
  }
}