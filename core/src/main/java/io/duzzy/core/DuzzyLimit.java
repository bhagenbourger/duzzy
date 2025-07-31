package io.duzzy.core;

public record DuzzyLimit(
    Long rows,
    Long size,
    Long duration
) {
  public DuzzyLimit() {
    this(null, null, null);
  }

  public DuzzyLimit withRows(Long rows) {
    return rows == null ? this : new DuzzyLimit(rows, size(), duration());
  }

  public DuzzyLimit withSize(Long size) {
    return size == null ? this : new DuzzyLimit(rows(), size, duration());
  }

  public DuzzyLimit withDuration(Long duration) {
    return duration == null ? this : new DuzzyLimit(rows(), size(), duration);
  }

  public boolean isReached(long rows, long size, long duration) {
    return (rows() == null && size() == null && duration() == null && rows >= 10)
        || (rows() != null && rows >= rows())
        || (size() != null && size >= size())
        || (duration() != null && duration >= duration() * 1000);
  }
}
