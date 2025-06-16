package io.duzzy.cli.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuzzyResultYamlFormatter implements DuzzyResultFormatter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DuzzyResultYamlFormatter.class);

  private static final ObjectMapper MAPPER = new YAMLMapper()
      .registerModule(new JavaTimeModule());

  @Override
  public String format(DuzzyResult duzzyResult) {
    try {
      return MAPPER.writer().writeValueAsString(duzzyResult);
    } catch (JsonProcessingException e) {
      LOGGER.warn(
          "An error occurred while formatting result in YAML, fallback on raw strategy",
          e
      );
      return duzzyResult.toString();
    }
  }
}
