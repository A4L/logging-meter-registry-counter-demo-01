package io.a4l.examples;


import io.micrometer.core.instrument.MeterRegistry;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public abstract class AbstractMeterPublishTest extends AbstractMeterTest {

  public AbstractMeterPublishTest(MeterRegistry meterRegistry, Logger log) {
    super(meterRegistry, log);
  }

  static Logger getSink(String name) {
    return LoggerFactory
        .getLogger(name);
  }

  static Duration getStep() {
    return Duration
        .ofMillis(1000L);
  }

  @Override
  @AfterEach
  void removeAllMeters() {
    try {
      Thread.sleep(getStep()
          .toMillis() + 500L);
    } catch (InterruptedException e) {
      log.error("", e);
    } finally {
      super.removeAllMeters();
    }
  }
}
