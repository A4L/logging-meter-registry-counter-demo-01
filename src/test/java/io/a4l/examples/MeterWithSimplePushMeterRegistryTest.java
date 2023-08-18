package io.a4l.examples;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.Meter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MeterWithSimplePushMeterRegistryTest extends AbstractMeterPublishTest {

  public MeterWithSimplePushMeterRegistryTest() {
    super(new SimplePushMeterRegistry(
        getTestSimplePushRegistryConfig(),
        Clock.SYSTEM,
        getTestSink()), log);
  }

  private static Consumer<List<Meter>> getTestSink() {
    return meters -> {
      meters.stream()
          .forEach(m -> getSink("simple-push-sink")
              .info("{}", m.getId().toString()));
    };
  }

  private static SimplePushRegistryConfig getTestSimplePushRegistryConfig() {
    Map<String, String> config = new HashMap<>();
    config.put("io.a4l.examples.registry.enabled", "true");
    config.put("io.a4l.examples.registry.mode", "cumulative");
    config.put("io.a4l.examples.registry.step", getStep().toString());
    config.put("io.a4l.examples.registry.threadFactory", "simple-push-worker");
    return new SimplePushRegistryConfig("io.a4l.examples.registry", config);
  }
}
