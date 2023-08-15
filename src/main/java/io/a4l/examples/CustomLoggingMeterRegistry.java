package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.cumulative.CumulativeCounter;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import io.micrometer.core.instrument.simple.CountingMode;
import io.micrometer.core.instrument.step.StepCounter;
import java.util.Optional;
import java.util.function.Consumer;


public class CustomLoggingMeterRegistry extends LoggingMeterRegistry {

  private final LoggingRegistryConfig config;
  private final Clock clock;


  public CustomLoggingMeterRegistry(
      LoggingRegistryConfig config,
      Clock clock,
      Consumer<String> loggingSink) {
    super(config, clock, loggingSink);
    this.config = config;
    this.clock = clock;
  }

  @Override
  protected Counter newCounter(Id id) {
    CountingMode countingMode = getCountingMode();
    switch (countingMode) {
      case CUMULATIVE:
        return new CumulativeCounter(id);
      case STEP:
        return new StepCounter(id, clock, config.step().toMillis());
      default:
        throw new UnsupportedOperationException(String
            .format("Unsupported counting mode '%s'",
                countingMode.name()));
    }
  }

  private CountingMode getCountingMode() {
    return Optional
        .ofNullable(config
            .get("counting.mode"))
        .map(CountingMode::valueOf)
        .orElse(CountingMode.CUMULATIVE);
  }
}
