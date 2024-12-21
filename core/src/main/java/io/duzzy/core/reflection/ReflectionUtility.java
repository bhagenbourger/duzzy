package io.duzzy.core.reflection;

import com.google.common.reflect.ClassPath;
import io.duzzy.core.provider.DuzzyProvider;
import io.duzzy.core.provider.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectionUtility {

  private ReflectionUtility() {
  }

  private static final String SEP = "build.classes.java.main.";
  private static final String DUZZY_NAME = "duzzy";

  private static Class<?> loadSafety(ClassPath.ClassInfo classInfo) {
    try {
      final String[] split = classInfo.getName().split(SEP);
      return Class.forName(split.length == 2 ? split[1] : split[0]);
    } catch (Throwable e) {
      return null;
    }
  }

  private static Stream<? extends Class<?>> loadDuzzyAnnotatedClasses(
      final Class<? extends Annotation> annotationClass
  ) throws IOException {
    return ClassPath
        .from(ClassLoader.getSystemClassLoader())
        .getAllClasses()
        .stream()
        .filter(c -> c.getName().contains(DUZZY_NAME))
        .map(ReflectionUtility::loadSafety)
        .filter(Objects::nonNull)
        .filter(c -> c.isAnnotationPresent(annotationClass));
  }

  public static List<Provider<?>> loadDuzzyProviders() throws IOException {
    return ReflectionUtility
        .loadDuzzyAnnotatedClasses(DuzzyProvider.class)
        .map(c -> {
          try {
            return (Provider<?>) c.getConstructor().newInstance();
          } catch (InstantiationException
                   | IllegalAccessException
                   | InvocationTargetException
                   | NoSuchMethodException e) {
            throw new RuntimeException(e);
          }
        })
        .collect(Collectors.toUnmodifiableList());
  }
}