package io.duzzy.core.field;

import static io.duzzy.test.Utility.RANDOM_FIELD_CONTEXT;
import static io.duzzy.test.Utility.providersFieldContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.duzzy.plugin.provider.constant.BooleanConstantProvider;
import io.duzzy.plugin.provider.constant.StringConstantProvider;
import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import java.util.List;
import org.junit.jupiter.api.Test;

public class FieldTest {

  public static void checkSometimesNull(Field field, int expected) {
    int cpt = 0;
    for (int i = 0; i < 1000; i++) {
      if (field.value(RANDOM_FIELD_CONTEXT.get()) == null) {
        cpt++;
      }
    }
    assertThat(Math.round(cpt / 100f)).isEqualTo(expected);
  }

  @Test
  void fieldNameCantBeNull() {
    assertThatThrownBy(() -> new Field(null, null, null, null, List.of()))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Field name can't be null or empty");
  }

  @Test
  void fieldNameCantBeEmpty() {
    assertThatThrownBy(() -> new Field("", null, null, null, List.of()))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Field name can't be null or empty");
  }

  @Test
  void fieldTypeCantBeNull() {
    assertThatThrownBy(() -> new Field("ok", null, null, null, null))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Field type can't be null");
  }

  @Test
  void fieldProvidersCantBeNull() {
    assertThatThrownBy(() -> new Field("ok", Type.STRING, null, null, null))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Providers can't be null or empty");
  }

  @Test
  void fieldProvidersCantBeEmpty() {
    assertThatThrownBy(() -> new Field("ok", Type.STRING, null, null, List.of()))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Providers can't be null or empty");
  }

  @Test
  void nullRateDefaultValueShouldBe0() {
    final Field field = new Field(
        "ok",
        Type.BOOLEAN,
        null,
        null,
        List.of(new BooleanConstantProvider(Boolean.TRUE))
    );

    assertThat(field.nullRate()).isEqualTo(0f);
  }

  @Test
  void nullRateMustBeBetween0and1() {
    assertThatThrownBy(() -> new Field("ok", Type.STRING, 2f, null, List.of()))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Field nullRate must be between 0 and 1");
  }

  @Test
  void corruptedRateDefaultValueShouldBe0() {
    final Field field = new Field(
        "ok",
        Type.BOOLEAN,
        null,
        null,
        List.of(new BooleanConstantProvider(Boolean.TRUE))
    );

    assertThat(field.corruptedRate()).isEqualTo(0f);
  }

  @Test
  void corruptedRateMustBeBetween0and1() {
    assertThatThrownBy(() -> new Field("ok", Type.STRING, null, 2f, List.of()))
        .isInstanceOf(java.lang.AssertionError.class)
        .hasMessage("Field corruptedRate must be between 0 and 1");
  }

  @Test
  void shouldBeNullable() {
    final Field field = new Field(
        "Test",
        Type.BOOLEAN,
        0.1f,
        null,
        List.of(new BooleanConstantProvider(Boolean.TRUE))
    );
    assertThat(field.isNullable()).isTrue();
  }

  @Test
  void shouldNotBeNullable() {
    final Field field = new Field(
        "Test",
        Type.BOOLEAN,
        0f,
        null,
        List.of(new BooleanConstantProvider(Boolean.TRUE))
    );
    assertThat(field.isNullable()).isFalse();
  }

  @Test
  void corrupted() {
    final Object value = new Field(
        "ok",
        Type.BOOLEAN,
        null,
        1f,
        List.of(new BooleanConstantProvider(Boolean.TRUE))
    ).value(providersFieldContext(false).get());

    assertThat(value).isInstanceOf(Long.class);
  }

  @Test
  void corruptedWithSchema() {
    final Object value = new Field(
        "ok",
        Type.STRING,
        null,
        1f,
        List.of(new StringConstantProvider("const"))
    ).value(providersFieldContext(true).get());

    assertThat(value).isInstanceOf(String.class);
    assertThat(value).isNotEqualTo("const");
  }

  @Test
  void alwaysNull() {
    final Field field = new Field(
        "test",
        Type.STRING,
        1f,
        0f,
        List.of(new AlphanumericRandomProvider())
    );
    checkSometimesNull(field, 10);
  }

  @Test
  void neverNull() {
    final Field field = new Field(
        "test",
        Type.STRING,
        0f,
        0f,
        List.of(new AlphanumericRandomProvider())
    );
    checkSometimesNull(field, 0);
  }

  @Test
  void sometimesNull() {
    final Field field = new Field(
        "test",
        Type.STRING,
        0.5f,
        0f,
        List.of(new AlphanumericRandomProvider())
    );
    checkSometimesNull(field, 5);
  }
}
