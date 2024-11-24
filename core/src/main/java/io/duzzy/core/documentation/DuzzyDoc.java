package io.duzzy.core.documentation;

import io.duzzy.core.reflection.ReflectionUtility;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DuzzyDoc {

  public static Map<DuzzyType, List<Documentation>> generate() throws IOException {
    return ReflectionUtility.loadDocumentation();
  }
}
