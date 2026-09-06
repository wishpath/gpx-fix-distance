package org.sa.util;

import org.sa.PointDto;

import java.time.Duration;
import java.util.List;

public class PrintUtil {

  public static void printPointsDetails(List<PointDto> points) {
    double totalDistanceMeters = 0;
    Duration totalDuration = Duration.ZERO;

    for (int i = 1; i < points.size(); i++) {
      PointDto previous = points.get(i - 1);
      PointDto current = points.get(i);
      double distanceMeters = DistanceUtil.measureDistanceMeters(previous, current);
      Duration duration = TimeUtil.measureDurationMillis(previous, current);
      totalDistanceMeters += distanceMeters;
      totalDuration = totalDuration.plus(duration);

      System.out.printf("%3d: %.2f m | total: %.2f m | %s | total: %s | %s min/km%n",
          i, distanceMeters, totalDistanceMeters, formatDuration(duration),
          formatDuration(totalDuration), formatPace(duration, distanceMeters));
    }
  }

  public static String formatDuration(Duration duration) {
    return "%d:%02d".formatted(duration.toMinutes(), duration.toSecondsPart());
  }

  public static String formatPace(Duration duration, double distanceMeters) {
    long secondsPerKilometer = Math.round(duration.toMillis() / 1000.0 / (distanceMeters / 1000));
    return "%d:%02d".formatted(secondsPerKilometer / 60, secondsPerKilometer % 60);
  }
}
