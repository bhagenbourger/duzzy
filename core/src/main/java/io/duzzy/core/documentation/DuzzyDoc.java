package io.duzzy.core.documentation;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DuzzyDoc {

    public static void main(String[] args) throws IOException {
        System.out.println(new DuzzyDoc().generate());
    }

    private static final String SEP = "build.classes.java.main.";

    private static Class<?> loadSafety(ClassPath.ClassInfo classInfo) {
        try {
            final String[] split = classInfo.getName().split(SEP);
            return Class.forName(split.length == 2 ? split[1] : split[0]);
        } catch (Throwable e) {
            return null;
        }
    }

    public Map<DuzzyType, List<Documentation>> generate() throws IOException {
        return ClassPath
                .from(ClassLoader.getSystemClassLoader())
                .getAllClasses()
                .stream()
                .filter(c -> c.getName().contains("duzzy"))
                .map(DuzzyDoc::loadSafety)
                .filter(Objects::nonNull)
                .filter(c -> c.isAnnotationPresent(Documentation.class))
                .map(c -> c.getAnnotation(Documentation.class))
                .collect(Collectors.groupingBy(Documentation::duzzyType));
    }
}
