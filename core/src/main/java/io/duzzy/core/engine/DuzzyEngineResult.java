package io.duzzy.core.engine;

public record DuzzyEngineResult(
    Long rows,
    Long size,
    Long duration
) {

  public DuzzyEngineResult() {
    this(null, null, null);
  }

  public DuzzyEngineResult {
    rows = rows == null ? Long.valueOf(0L) : rows;
    size = size == null ? Long.valueOf(0L) : size;
    duration = duration == null ? Long.valueOf(0L) : duration;
  }

  public DuzzyEngineResult sum(DuzzyEngineResult other) {
    return new DuzzyEngineResult(
        this.rows + other.rows,
        this.size + other.size,
        this.duration + other.duration
    );
  }
}
