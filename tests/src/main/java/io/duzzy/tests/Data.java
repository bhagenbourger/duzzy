package io.duzzy.tests;

import io.duzzy.core.DataItem;
import io.duzzy.core.DataItems;
import io.duzzy.core.column.ColumnType;
import java.util.List;

public class Data {
  public static final String KEY_C1 = "c1";
  public static final String KEY_C2 = "c2";
  public static final Integer INTEGER_ONE = 1;
  public static final Integer INTEGER_TWO = 2;
  public static final String STRING_ONE = "one";
  public static final String STRING_TWO = "two";

  public static DataItems getDataOne() {
    return new DataItems(List.of(
        new DataItem(KEY_C1, ColumnType.INTEGER, INTEGER_ONE),
        new DataItem(KEY_C2, ColumnType.STRING, STRING_ONE)
    ));
  }

  public static DataItems getDataTwo() {
    return new DataItems(List.of(
        new DataItem(KEY_C1, ColumnType.INTEGER, INTEGER_TWO),
        new DataItem(KEY_C2, ColumnType.STRING, STRING_TWO)
    ));
  }
}
