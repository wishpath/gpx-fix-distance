package org.sa.util;

import org.sa.PointDto;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

public class TimeUtil {
  private static final long MILLIS_PER_MINUTE = 60_000;


  public static Duration measureDurationMillis(PointDto first, PointDto second) {
    return Duration.between(first.time(), second.time());
  }

  public static double measureDurationMinutes(PointDto first, PointDto second) {
    return Duration.ofMillis(measureDurationMillis(first, second).toMillis() / MILLIS_PER_MINUTE).toMinutes();
  }

  public static Duration measureTotalTime(List<PointDto> points) {
    return IntStream.range(1, points.size()).mapToObj(i -> TimeUtil.measureDurationMillis(points.get(i - 1), points.get(i))).reduce(Duration.ZERO, Duration::plus);
  }
}