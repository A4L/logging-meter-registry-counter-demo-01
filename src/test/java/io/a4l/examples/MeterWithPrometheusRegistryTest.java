package io.a4l.examples;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MeterWithPrometheusRegistryTest extends AbstractMeterTest {

  public MeterWithPrometheusRegistryTest() {
    super(
        new PrometheusMeterRegistry(
            new PrometheusConfig() {
              @Override
              public String get(String key) {
                return null;
              }

              @Override
              public Duration step() {
                return Duration
                    .ofMillis(1L);
              }

            }),
        log);
  }
}
