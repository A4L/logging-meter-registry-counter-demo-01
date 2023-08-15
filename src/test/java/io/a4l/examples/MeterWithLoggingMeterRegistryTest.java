package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


@Slf4j
public class MeterWithLoggingMeterRegistryTest extends AbstractMeterTest {

  public MeterWithLoggingMeterRegistryTest() {
    super(new LoggingMeterRegistry(LoggingRegistryConfig.DEFAULT, Clock.SYSTEM,
        log::info), log);
  }

  @Override
  @Test
  @Disabled
  void work5() {
    super.work5();
  }
}
