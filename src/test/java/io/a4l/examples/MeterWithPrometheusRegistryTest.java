package io.a4l.examples;

import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MeterWithPrometheusRegistryTest extends AbstractMeterTest {

  public MeterWithPrometheusRegistryTest() {
    super(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT), log);
  }
}
