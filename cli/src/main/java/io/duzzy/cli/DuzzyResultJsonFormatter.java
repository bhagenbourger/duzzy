package io.duzzy.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuzzyResultJsonFormatter implements DuzzyResultFormatter {

  private static final Logger logger = LoggerFactory.getLogger(DuzzyResultJsonFormatter.class);

  private static final ObjectMapper MAPPER = new ObjectMapper()
      .registerModule(new JavaTimeModule());

  @Override
  public String format(DuzzyResult duzzyResult) {
    try {
      return MAPPER.writer().writeValueAsString(duzzyResult);
    } catch (JsonProcessingException e) {
      logger.warn(
          "An error occurred while formatting result in JSON, fallback on raw strategy",
          e
      );
      return duzzyResult.toString();
    }
  }
}
