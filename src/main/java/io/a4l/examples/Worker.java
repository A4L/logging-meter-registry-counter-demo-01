package io.a4l.examples;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class Worker {

  private final MeterRegistry meterRegistry;


  public void work() {
    meterRegistry
        .counter("work", "foo", "bar")
        .increment();
    log.info("working ...");
  }
}
