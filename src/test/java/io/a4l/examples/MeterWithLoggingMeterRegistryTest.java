package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MeterWithLoggingMeterRegistryTest extends AbstractMeterTest {

  public MeterWithLoggingMeterRegistryTest() {
    super(
        new LoggingMeterRegistry(
            new LoggingRegistryConfig() {

              @Override
              public String get(String key) {
                return null;
              }

              @Override
              public Duration step() {
                return Duration
                    .ofMillis(1000L);
              }
            }, Clock.SYSTEM, log::info),
        log);
  }
}
