package io.duzzy.plugin.provider.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.duzzy.core.provider.constant.ConstantProvider;
import io.duzzy.core.provider.corrupted.FloatCorruptedProvider;

public class FloatConstantProvider extends ConstantProvider<Float>
    implements FloatCorruptedProvider {

  @JsonCreator
  public FloatConstantProvider(
      @JsonProperty("value") Float value
  ) {
    super(value);
  }
}
