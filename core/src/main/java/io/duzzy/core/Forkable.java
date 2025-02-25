package io.duzzy.core;

public interface Forkable<F> {

  Integer MONO_THREAD = 1;

  F fork(Long threadId) throws Exception;

  default int maxThread() {
    return Integer.MAX_VALUE;
  }
}
