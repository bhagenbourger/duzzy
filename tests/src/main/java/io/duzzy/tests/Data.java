package io.duzzy.tests;

import io.duzzy.core.DuzzyCell;
import io.duzzy.core.DuzzyRow;
import io.duzzy.core.DuzzyRowKey;
import io.duzzy.core.field.Type;
import java.util.List;
import java.util.Optional;

public class Data {
  public static final String KEY_C1 = "c1";
  public static final String KEY_C2 = "c2";
  public static final Integer INTEGER_ONE = 1;
  public static final Integer INTEGER_TWO = 2;
  public static final String STRING_ONE = "one";
  public static final String STRING_TWO = "two";

  private static final List<DuzzyCell> CELLS_ONE = List.of(
      new DuzzyCell(KEY_C1, Type.INTEGER, INTEGER_ONE),
      new DuzzyCell(KEY_C2, Type.STRING, STRING_ONE)
  );

  private static final List<DuzzyCell> CELLS_TWO = List.of(
      new DuzzyCell(KEY_C1, Type.INTEGER, INTEGER_TWO),
      new DuzzyCell(KEY_C2, Type.STRING, STRING_TWO)
  );
  
  public static DuzzyRow getDataOne() {
    return new DuzzyRow(CELLS_ONE);
  }

  public static DuzzyRow getDataOneWithKey() {
    return new DuzzyRow(
        new DuzzyRowKey(Optional.of("key1")),
        CELLS_ONE
    );
  }

  public static DuzzyRow getDataTwo() {
    return new DuzzyRow(CELLS_TWO);
  }

  public static DuzzyRow getDataTwoWithKey() {
    return new DuzzyRow(
        new DuzzyRowKey(Optional.of("key2")),
        CELLS_TWO
    );
  }

  public static String getDataOneAsJsonString() {
    return "{\"c1\":1,\"c2\":\"one\"}";
  }

  public static String getDataTwoAsJsonString() {
    return "{\"c1\":2,\"c2\":\"two\"}";
  }
}
