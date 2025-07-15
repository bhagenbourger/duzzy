package io.duzzy.tests;

import io.duzzy.core.DuzzyCell;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.field.Type;
import java.util.List;

public class Data {
  public static final String KEY_C1 = "c1";
  public static final String KEY_C2 = "c2";
  public static final Integer INTEGER_ONE = 1;
  public static final Integer INTEGER_TWO = 2;
  public static final String STRING_ONE = "one";
  public static final String STRING_TWO = "two";

  public static DuzzyRow getDataOne() {
    return new DuzzyRow(
        List.of(
            new DuzzyCell(KEY_C1, Type.INTEGER, INTEGER_ONE),
            new DuzzyCell(KEY_C2, Type.STRING, STRING_ONE)
        )
    );
  }

  public static DuzzyRow getDataTwo() {
    return new DuzzyRow(
        List.of(
            new DuzzyCell(KEY_C1, Type.INTEGER, INTEGER_TWO),
            new DuzzyCell(KEY_C2, Type.STRING, STRING_TWO)
        )
    );
  }

  public static String getDataOneAsJsonString() {
    return "{\"c1\":1,\"c2\":\"one\"}";
  }

  public static String getDataTwoAsJsonString() {
    return "{\"c1\":2,\"c2\":\"two\"}";
  }
}
