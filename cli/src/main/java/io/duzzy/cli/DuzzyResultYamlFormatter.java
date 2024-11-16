package io.duzzy.cli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.duzzy.core.DuzzyResult;

public class DuzzyResultYamlFormatter implements DuzzyResultFormatter {
    private static final ObjectMapper MAPPER = new YAMLMapper()
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
