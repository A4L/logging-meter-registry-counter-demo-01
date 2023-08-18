package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.Meter.Type;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import io.micrometer.core.instrument.distribution.pause.PauseDetector;
import io.micrometer.core.instrument.push.PushMeterRegistry;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimplePushMeterRegistry extends PushMeterRegistry {

  private final SimplePushRegistryConfig config;
  private final Consumer<List<Meter>> sink;
  private final SimpleMeterRegistryDelegate delegate =
      new SimpleMeterRegistryDelegate();

  public SimplePushMeterRegistry(
      SimplePushRegistryConfig config, Clock clock, Consumer<List<Meter>> sink) {
    super(config, clock);
    this.config = config;
    this.sink = sink;
    start(new NamedThreadFactory(getThreadFactoryName()));
  }

  private String getThreadFactoryName() {
    return Optional
        .ofNullable(config
            .get("threadFactory"))
        .orElse(getClass()
            .getSimpleName());
  }

  @Override
  protected void publish() {
    boolean enabled = config.enabled();
    log.debug("{}.publish(enabled={})", getClass().getSimpleName(), enabled);
    if (enabled) {
      sink.accept(getMeters());
    }
  }

  @Override
  protected <T> Gauge newGauge(Id id, T obj, ToDoubleFunction<T> valueFunction) {
    return delegate.callNewGauge(id, obj, valueFunction);
  }

  @Override
  protected Counter newCounter(Id id) {
    return delegate.callNewCounter(id);
  }

  @Override
  protected Timer newTimer(Id id, DistributionStatisticConfig distributionStatisticConfig, PauseDetector pauseDetector) {
    return delegate.newTimerInstace(id, distributionStatisticConfig, pauseDetector);
  }

  @Override
  protected DistributionSummary newDistributionSummary(Id id, DistributionStatisticConfig distributionStatisticConfig, double scale) {
    return delegate.callNewDistributionSummary(id, distributionStatisticConfig, scale);
  }

  @Override
  protected Meter newMeter(Id id, Type type, Iterable<Measurement> measurements) {
    return delegate.callNewMeter(id, type, measurements);
  }

  @Override
  protected <T> FunctionTimer newFunctionTimer(Id id, T obj, ToLongFunction<T> countFunction, ToDoubleFunction<T> totalTimeFunction,
      TimeUnit totalTimeFunctionUnit) {
    return delegate.callNewFunctionTimer(id, obj, countFunction, totalTimeFunction, totalTimeFunctionUnit);
  }

  @Override
  protected <T> FunctionCounter newFunctionCounter(Id id, T obj, ToDoubleFunction<T> countFunction) {
    return delegate.callNewFunctionCounter(id, obj, countFunction);
  }

  @Override
  protected TimeUnit getBaseTimeUnit() {
    return delegate.callGetBaseTimeUnit();
  }

  @Override
  protected DistributionStatisticConfig defaultHistogramConfig() {
    return delegate.callDefaultHistogramConfig();
  }
}
