package io.duzzy.core;

public interface Forkable<F> {

  Integer MONO_THREAD = 1;

  F fork(long id) throws Exception;

  default int maxThread() {
    return Integer.MAX_VALUE;
  }
}
