package io.a4l.examples;

import io.micrometer.core.instrument.config.validate.PropertyValidator;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.push.PushRegistryConfig;
import io.micrometer.core.instrument.simple.SimpleConfig;
import java.time.Duration;
import java.util.Map;


public class SimplePushRegistryConfig extends MapBasedRegistryConfig
    implements SimpleConfig, PushRegistryConfig {

  public SimplePushRegistryConfig(
      String prefix, Map<String, String> config) {
    super(prefix, config);
  }

  @Override
  public Validated<?> validate() {
    Validated<?> validated = SimpleConfig.super.validate();
    if (validated.isValid()) {
      return PushRegistryConfig.super.validate();
    }
    return validated;
  }

  @Override
  public Duration step() {
    return PropertyValidator
        .getDuration(this, "step")
        .orElseGet(() -> Duration.ofSeconds(5L));
  }
}
