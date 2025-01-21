package io.duzzy.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

public class Helper {

  public static File getFromResources(Class<?> clazz, String name) {
    final URL resource = clazz.getClassLoader().getResource(name);
    assert resource != null;
    return new File(resource.getFile());
  }

  public static File createTempFile(String prefix) throws IOException {
    final File tmp = File.createTempFile(prefix, ".tmp");
    tmp.deleteOnExit();
    return tmp;
  }

  public static void deleteDirectory(Path path) throws IOException {
    if (Files.exists(path)) {
      try (Stream<Path> paths = Files.walk(path)) {
        paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
      }
    }
  }
}
