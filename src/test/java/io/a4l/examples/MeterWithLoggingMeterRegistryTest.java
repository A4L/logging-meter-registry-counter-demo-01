package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


@Slf4j
public class MeterWithLoggingMeterRegistryTest extends AbstractMeterPublishTest {

  public MeterWithLoggingMeterRegistryTest() {
    super(new LoggingMeterRegistry(new LoggingRegistryConfig() {

      @Override
      public String get(String key) {
        return null;
      }

      @Override
      public Duration step() {
        return getStep();
      }

    }, Clock.SYSTEM,
        getSink("logging-sink")::info), log);
  }

  @Override
  @Test
  @Disabled
  void work5() {
    super.work5();
  }
}
