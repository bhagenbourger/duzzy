package io.duzzy.cli.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyResult;

public class DuzzyResultXmlFormatter implements DuzzyResultFormatter {
    private static final ObjectMapper MAPPER = new XmlMapper()
            .registerModule(new JavaTimeModule());

    @Override
    public String format(DuzzyResult duzzyResult) {
        try {
            return MAPPER.writer().writeValueAsString(duzzyResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e); //TODO: log error and fallback on raw strategy
//            return duzzyResult.toString();
        }
    }
}
