package io.a4l.examples;

import io.micrometer.core.instrument.config.MeterRegistryConfig;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapBasedRegistryConfig implements MeterRegistryConfig {

  private final String prefix;
  private final Map<String, String> config;

  @Override
  public String prefix() {
    return prefix;
  }

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
    if (null == key) {
      return prefix;
    }
    if (key.startsWith(prefix)) {
      return key;
    }
    return String
        .join(".", prefix, key);
  }
}
