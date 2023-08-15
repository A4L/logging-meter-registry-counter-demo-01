package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MeterWithSimpleMeterRegistryTest extends AbstractMeterTest {

  public MeterWithSimpleMeterRegistryTest() {
    super(new SimpleMeterRegistry(SimpleConfig.DEFAULT, Clock.SYSTEM), log);
  }
}
