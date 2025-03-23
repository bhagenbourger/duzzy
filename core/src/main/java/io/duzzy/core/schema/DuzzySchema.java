package io.duzzy.core.schema;

import io.duzzy.core.documentation.Documentation;
import io.duzzy.core.documentation.DuzzyType;
import io.duzzy.core.field.Field;
import java.util.List;

@Documentation(
    identifier = "io.duzzy.core.schema.DuzzySchema",
    description = "Duzzy schema is the default input schema "
        + "in which you can specify whatever you want into the output",
    duzzyType = DuzzyType.DUZZY_SCHEMA,
    parameters = {},
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
    List<Field> fields
) {
}
