package io.a4l.examples;


import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Measurement;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.Meter.Id;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Statistic;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.search.Search;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;


public abstract class AbstractMeterTest {

  @RequiredArgsConstructor
  @Builder
  @Getter
  protected static class ExpectedMeterData {
    private final String meterName;
    private final Class<? extends Meter> meterType;
    private final List<Statistic> statistics;
    private final List<Measurement> measurements;
    private final List<Tag> tags;
  }


  private final MeterRegistry meterRegistry;
  private final Worker worker;
  private final Logger log;


  public AbstractMeterTest(MeterRegistry meterRegistry, Logger log) {
    this.meterRegistry = meterRegistry;
    this.worker = new Worker(meterRegistry);
    this.log = log;
  }

  @AfterEach
  void removeAllMeters() {

    List<Meter> meters = meterRegistry
        .getMeters();

    meters
        .stream()
        .forEach(e -> {
          log.debug("Meter removed: {}",
              meterRegistry
                  .remove(e)
                  .getId());
        });

    log.debug("Total meter removed: {}", meters.size());
  }


  @Test
  void work5() {
    execute(
        () -> {
          worker.work();
          worker.work();
          worker.work();
          worker.work();
          worker.work();
        },
        ExpectedMeterData
            .builder()
            .meterName("work")
            .meterType(Counter.class)
            .statistics(List.of(Statistic.COUNT))
            .measurements(List.of(new Measurement(() -> 5d, Statistic.COUNT)))
            .tags(List.of(Tag.of("foo", "bar")))
            .build());
  }

  private void execute(
      Runnable runnanle, ExpectedMeterData... expectedMeterData) {

    try {
      runnanle.run();
    } catch (Throwable e) {
      log.error("Error thrown", e);
    }

    for (ExpectedMeterData expected : expectedMeterData) {

      String expectedMeterName = expected.getMeterName();
      Class<? extends Meter> expectedMeterType = expected.getMeterType();
      List<Statistic> expectedStatistics = expected.getStatistics();
      List<Measurement> expectedMeasurements = expected.getMeasurements();
      List<Tag> expectedTags = getExpectedTags(expected);

      Meter meter = findOneByExactTagkeys(expectedMeterName, expectedTags);

      Assertions.assertNotNull(meter, expectedMeterName);

      Id id = meter.getId();

      Assertions.assertEquals(expectedMeterName, id.getName());
      Assertions.assertTrue(expectedMeterType
          .isAssignableFrom(meter.getClass()));

      Map<Statistic, Measurement> actualMeasurments = StreamSupport
          .stream(Spliterators
              .spliteratorUnknownSize(meter.measure()
                  .iterator(), Spliterator.ORDERED),
              false)
          .collect(Collectors
              .toMap(Measurement::getStatistic, Function.identity()));

      actualMeasurments
          .keySet()
          .stream()
          .forEach(actualStatistic -> {
            Assertions
                .assertTrue(
                    expectedStatistics
                        .contains(actualStatistic),
                    () -> {
                      return String
                          .format("%s/%s statistic not available, expected: %s",
                              expectedMeterType
                                  .getSimpleName(),
                              expectedMeterName,
                              actualStatistic);
                    });
          });


      expectedMeasurements
          .stream()
          .collect(Collectors
              .toMap(Measurement::getStatistic, Function.identity()))
          .forEach((expectedStatistic, expectedMeasurement) -> {
            Measurement actualMeasurment = actualMeasurments
                .get(expectedStatistic);
            Assertions
                .assertNotNull(
                    actualMeasurment,
                    () -> {
                      return String
                          .format("%s/%s measurement not available, expected: %s",
                              expectedMeterType
                                  .getSimpleName(),
                              expectedMeterName,
                              expectedStatistic);
                    });

            double expectedValue = expectedMeasurement.getValue();
            double actualValue = actualMeasurment.getValue();
            Assertions
                .assertEquals(
                    expectedValue,
                    actualValue,
                    () -> {
                      return String
                          .format("%s/%s measurement does not have the expected value",
                              expectedMeterType
                                  .getSimpleName(),
                              expectedMeterName);
                    });
          });

      List<Tag> tags = id.getTags();
      if (!tags.isEmpty()) {
        Assertions
            .assertNotNull(expectedTags, "No tags expected");
        Assertions
            .assertEquals(expectedTags.size(), tags.size(), () -> {
              return String
                  .format("expectedTags=%s, actualTags=%s",
                      expectedTags.toString(), meter.getId().getTags().toString());
            });
      }

      expectedTags
          .stream()
          .collect(Collectors
              .toMap(Tag::getKey, Tag::getValue))
          .forEach((expectedKey, expectedValue) -> {
            String actualValue = id.getTag(expectedKey);
            Assertions
                .assertEquals(
                    expectedValue,
                    actualValue,
                    () -> {
                      return String
                          .format("%s/%s tag %s does not have the expected value",
                              expectedMeterType
                                  .getSimpleName(),
                              expectedMeterName,
                              expectedKey);
                    });
          });
    }
  }

  private List<Tag> getExpectedTags(ExpectedMeterData expected) {
    return Optional
        .ofNullable(expected.getTags())
        .orElseGet(Collections::emptyList);
  }

  private Meter findOneByExactTagkeys(
      String expectedMeterName, List<Tag> expectedTags) {
    for (Meter meter : Search
        .in(meterRegistry)
        .name(expectedMeterName)
        .meters()) {
      List<Tag> actualTags = meter.getId()
          .getTags();
      if (exactMatchByKeys(expectedTags, actualTags)) {
        return meter;
      }
    }
    return null;
  }

  private boolean exactMatchByKeys(
      List<Tag> expectedTags, List<Tag> actualTags) {
    Set<String> expectedKeySet = toKeySet(expectedTags);
    Set<String> actualKeySet = toKeySet(actualTags);
    return expectedKeySet.size() == actualKeySet.size() &&
        expectedKeySet.containsAll(actualKeySet);
  }

  private Set<String> toKeySet(List<Tag> tags) {
    return tags
        .stream()
        .map(Tag::getKey)
        .collect(Collectors
            .toSet());
  }
}
