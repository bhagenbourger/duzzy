package io.duzzy.plugin.provider.random;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.duzzy.core.field.FieldContext;
import io.duzzy.core.provider.Provider;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;

@Documentation(
    identifier = "io.duzzy.plugin.provider.random.BooleanRandomProvider",
    description = "Provide a random boolean value",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.PROVIDER,
    nativeSupport = true,
    example = """
        ---
        identifier: "io.duzzy.plugin.provider.random.BooleanRandomProvider"
        """
)
public class BooleanRandomProvider implements Provider<Boolean> {

  @JsonCreator
  public BooleanRandomProvider() {
  }

  @Override
  public Boolean value(FieldContext fieldContext) {
    return fieldContext.random().nextBoolean();
  }

  @Override
  public Boolean corruptedValue(FieldContext fieldContext) {
    return value(fieldContext);
  }
}
