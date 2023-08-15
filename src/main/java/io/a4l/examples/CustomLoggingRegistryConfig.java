package io.a4l.examples;

import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLoggingRegistryConfig implements LoggingRegistryConfig {

  private final Map<String, String> config;

  @Override
  public String get(String key) {
    if (null == config) {
      return null;
    }
    return config
        .get(withPrefix(key));
  }

  private String withPrefix(String key) {
    String prefix = prefix();
    if (null == prefix) {
      return key;
    }
    return String
        .join(".", prefix, key);
  }
}
