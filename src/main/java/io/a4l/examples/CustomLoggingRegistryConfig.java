package io.a4l.examples;

import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import java.util.Map;

public class CustomLoggingRegistryConfig
    extends MapBasedRegistryConfig implements LoggingRegistryConfig {

  public CustomLoggingRegistryConfig(Map<String, String> config) {
    super("logging", config);
  }
}
