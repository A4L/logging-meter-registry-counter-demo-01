package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.simple.CountingMode;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MeterWithCustomLoggingMeterRegistryTest extends AbstractMeterTest {

  public MeterWithCustomLoggingMeterRegistryTest() {
    super(new CustomLoggingMeterRegistry(
        new CustomLoggingRegistryConfig(Collections
            .singletonMap("logging.counting.mode",
                CountingMode.CUMULATIVE.name())),
        Clock.SYSTEM,
        log::info), log);
  }
}
