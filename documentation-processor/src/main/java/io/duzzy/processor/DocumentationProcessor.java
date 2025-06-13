package io.duzzy.processor;

import static io.duzzy.core.documentation.DuzzyDoc.FILENAME;
import static io.duzzy.core.documentation.DuzzyDoc.MAPPER;
import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

import io.duzzy.core.documentation.Documentation;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.apache.logging.log4j.util.Strings;

@SupportedAnnotationTypes("io.duzzy.core.documentation.Documentation")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class DocumentationProcessor extends AbstractProcessor {

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    final Messager messager = processingEnv.getMessager();
    messager.printMessage(NOTE, "Processing documentation annotations...");
    try {
      final Set<? extends Element> elements =
          roundEnv.getElementsAnnotatedWith(Documentation.class);
      if (elements.isEmpty()) {
        messager.printMessage(NOTE, "Documentation annotations has no elements to process");
        return false;
      }
      final List<Documentation> documentations = elements
          .stream()
          .map(e -> e.getAnnotation(Documentation.class))
          .filter(Objects::nonNull)
          .toList();
      final FileObject fileObject = processingEnv.getFiler().createResource(
          StandardLocation.CLASS_OUTPUT,
          Strings.EMPTY,
          FILENAME
      );
      try (final OutputStream outputStream = fileObject.openOutputStream()) {
        outputStream.write(MAPPER.writer().writeValueAsBytes(documentations));
      }
      messager.printMessage(NOTE, "Documentation annotations processed");
      return true;
    } catch (IOException e) {
      messager.printMessage(ERROR,
          "An error occurred while processing documentation annotations: " + e.getMessage());
      return false;
    }
  }
}
