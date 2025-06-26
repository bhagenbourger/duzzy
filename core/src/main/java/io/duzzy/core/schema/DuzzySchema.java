package io.duzzy.core.schema;

import io.duzzy.core.field.Field;
import io.duzzy.core.field.Type;
import io.duzzy.documentation.Documentation;
import io.duzzy.documentation.DuzzyType;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.provider.random.BooleanRandomProvider;
import io.duzzy.plugin.provider.random.DoubleRandomProvider;
import java.util.List;
import java.util.Optional;

@Documentation(
    identifier = "io.duzzy.core.schema.DuzzySchema",
    description = "Duzzy schema is the default input schema "
        + "in which you can specify whatever you want into the output",
    module = "io.duzzy.core",
    duzzyType = DuzzyType.DUZZY_SCHEMA,
    nativeSupport = true,
    example = """
        ---
        fields:
          - name: stringConstant
            type: STRING
            null_rate: 0.5
            corrupted_rate: 0.5
            providers:
              - identifier: "io.duzzy.plugin.provider.constant.StringConstantProvider"
                value: myConstant
          - name: stringListConstant
            type: STRING
            null_rate: 0.5
            corrupted_rate: 0.5
            providers:
              - identifier: "io.duzzy.plugin.provider.constant.StringListConstantProvider"
                values: [ "one", "two", "three" ]
          - name: stringWeightedListConstant
            type: STRING
            null_rate: 0.5
            corrupted_rate: 0.5
            providers:
              - identifier: "io.duzzy.plugin.provider.constant.StringWeightedListConstantProvider"
                values:
                  - value: first
                    weight: 1
                  - value: second
                    weight: 2
                  - value: third
                    weight: 3
          - name: longIncrement
            type: LONG
            null_rate: 0.5
            corrupted_rate: 0.5
            providers:
              - identifier: "io.duzzy.plugin.provider.increment.LongIncrementProvider"
                start: 100
                step: 10
          - name: integerRandom
            type: INTEGER
            null_rate: 0.5
            corrupted_rate: 0.5
            providers:
              - identifier: "io.duzzy.plugin.provider.random.IntegerRandomProvider"
                min: 50
                max: 100
        """
)
public record DuzzySchema(
    Optional<Field> rowKey,
    List<Field> fields
) {

  private static final List<Field> DEFAULT_FIELDS = List.of(
      new Field(
          "BooleanRandomProvider",
          Type.BOOLEAN,
          0f,
          0f,
          List.of(new BooleanRandomProvider())
      ),
      new Field(
          "AlphanumericRandomProvider",
          Type.STRING,
          0f,
          0f,
          List.of(new AlphanumericRandomProvider())
      ),
      new Field(
          "DoubleRandomProvider",
          Type.DOUBLE,
          0f,
          0f,
          List.of(new DoubleRandomProvider())
      )
  );

  public DuzzySchema {
    fields = fields == null || fields.isEmpty() ? DEFAULT_FIELDS : fields;
  }
}
