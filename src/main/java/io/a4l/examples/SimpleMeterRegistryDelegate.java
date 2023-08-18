package io.a4l.examples;

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
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import java.util.concurrent.TimeUnit;
import java.util.function.ToDoubleFunction;
import java.util.function.ToLongFunction;


public class SimpleMeterRegistryDelegate extends SimpleMeterRegistry {

  public <T> Gauge callNewGauge(Id id, T obj, ToDoubleFunction<T> valueFunction) {
    return super.newGauge(id, obj, valueFunction);
  }

  public Counter callNewCounter(Id id) {
    return super.newCounter(id);
  }

  public Timer newTimerInstace(Id id, DistributionStatisticConfig distributionStatisticConfig, PauseDetector pauseDetector) {
    return super.newTimer(id, distributionStatisticConfig, pauseDetector);
  }

  public DistributionSummary callNewDistributionSummary(Id id, DistributionStatisticConfig distributionStatisticConfig, double scale) {
    return super.newDistributionSummary(id, distributionStatisticConfig, scale);
  }

  public Meter callNewMeter(Id id, Type type, Iterable<Measurement> measurements) {
    return super.newMeter(id, type, measurements);
  }

  public <T> FunctionTimer callNewFunctionTimer(Id id, T obj, ToLongFunction<T> countFunction, ToDoubleFunction<T> totalTimeFunction,
      TimeUnit totalTimeFunctionUnit) {
    return super.newFunctionTimer(id, obj, countFunction, totalTimeFunction, totalTimeFunctionUnit);
  }

  public <T> FunctionCounter callNewFunctionCounter(Id id, T obj, ToDoubleFunction<T> countFunction) {
    return super.newFunctionCounter(id, obj, countFunction);
  }

  public TimeUnit callGetBaseTimeUnit() {
    return super.getBaseTimeUnit();
  }

  public DistributionStatisticConfig callDefaultHistogramConfig() {
    return super.defaultHistogramConfig();
  }
}
