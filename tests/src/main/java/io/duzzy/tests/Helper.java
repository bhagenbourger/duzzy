package io.duzzy.tests;

import java.io.File;
import java.io.IOException;
import java.net.URL;

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
}
