package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.DoubleCorruptedProvider;

public class DoubleConstantProvider extends ConstantProvider<Double>
    implements DoubleCorruptedProvider {

  @JsonCreator
  public DoubleConstantProvider(
      @JsonProperty("value") Double value
  ) {
    super(value);
  }
}
