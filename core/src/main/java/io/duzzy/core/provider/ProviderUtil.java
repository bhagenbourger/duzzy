package io.duzzy.core.provider;

import io.duzzy.plugin.provider.random.AlphanumericRandomProvider;
import io.duzzy.plugin.provider.random.BigDecimalRandomProvider;
import io.duzzy.plugin.provider.random.BooleanRandomProvider;
import io.duzzy.plugin.provider.random.DoubleRandomProvider;
import io.duzzy.plugin.provider.random.FloatRandomProvider;
import io.duzzy.plugin.provider.random.InstantRandomProvider;
import io.duzzy.plugin.provider.random.IntegerRandomProvider;
import io.duzzy.plugin.provider.random.LocalDateRandomProvider;
import io.duzzy.plugin.provider.random.LocalDateTimeRandomProvider;
import io.duzzy.plugin.provider.random.LocalTimeRandomProvider;
import io.duzzy.plugin.provider.random.LongRandomProvider;
import io.duzzy.plugin.provider.random.ShortRandomProvider;
import io.duzzy.plugin.provider.random.SqlDateRandomProvider;
import io.duzzy.plugin.provider.random.SqlTimeRandomProvider;
import io.duzzy.plugin.provider.random.UuidRandomProvider;
import java.util.List;
import java.util.Random;

public class ProviderUtil {

  private static final int LEFT_LIMIT = 48; // numeral '0'
  private static final int RIGHT_LIMIT = 122; // letter 'z'

  private ProviderUtil() {
  }

  public static final List<Provider<?>> RANDOM_PROVIDERS =
      List.of(
          new AlphanumericRandomProvider(),
          new BigDecimalRandomProvider(),
          new BooleanRandomProvider(),
          new DoubleRandomProvider(),
          new FloatRandomProvider(),
          new InstantRandomProvider(),
          new IntegerRandomProvider(),
          new LocalDateRandomProvider(),
          new LocalDateTimeRandomProvider(),
          new LocalTimeRandomProvider(),
          new LongRandomProvider(),
          new ShortRandomProvider(),
          new SqlDateRandomProvider(),
          new SqlTimeRandomProvider(),
          new UuidRandomProvider()
      );

  public static String randomString(Random random, int length) {
    return random
        .ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(length)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();
  }
}
