package io.duzzy.cli.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DuzzyResultXmlFormatter implements DuzzyResultFormatter {

  private static final Logger LOGGER = LoggerFactory.getLogger(DuzzyResultXmlFormatter.class);

  private static final ObjectMapper MAPPER = new XmlMapper()
      .registerModule(new JavaTimeModule());

  @Override
  public String format(DuzzyResult duzzyResult) {
    try {
      return MAPPER.writer().writeValueAsString(duzzyResult);
    } catch (JsonProcessingException e) {
      LOGGER.warn(
          "An error occurred while formatting result in XML, fallback on raw strategy",
          e
      );
      return duzzyResult.toString();
    }
  }
}
