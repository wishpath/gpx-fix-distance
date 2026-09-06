package org.sa.util;

import org.sa.PointDto;

import java.time.Duration;
import java.util.List;
import java.util.stream.IntStream;

public class PaceUtil {
  private static final double MIN_VALID_PACE_MINUTES_PER_KM = 3;
  private static final double MAX_VALID_PACE_MINUTES_PER_KM = 9;

  public static boolean isPaceTotallyOffForMostPoints(List<PointDto> points) {
    long offPacePoints = IntStream.range(1, points.size()).filter(i -> isPaceOff(points.get(i - 1), points.get(i))).count();
    return offPacePoints > (points.size() * 0.5);
  }

  private static boolean isPaceOff(PointDto first, PointDto second) {
    double distanceKilometers = DistanceUtil.measureDistanceKilometers(first, second);
    double durationMinutes = TimeUtil.measureDurationMinutes(first, second);
    double paceMinutesPerKm = durationMinutes / distanceKilometers;
    return paceMinutesPerKm < MIN_VALID_PACE_MINUTES_PER_KM || paceMinutesPerKm > MAX_VALID_PACE_MINUTES_PER_KM;
  }

  public static void evenOutPaceBetweenEachPoints(List<PointDto> points, Duration targetDuration) {
    double totalDistanceMeters = DistanceUtil.measureTotalDistanceMeters(points);
    PointDto firstPoint = points.getFirst();

    for (int i = 1; i < points.size(); i++) {
      double accumulatedDistanceMeters = IntStream.rangeClosed(1, i)
          .mapToDouble(index -> DistanceUtil.measureDistanceMeters(points.get(index - 1), points.get(index)))
          .sum();

      long elapsedMillis = Math.round(
          targetDuration.toMillis() * accumulatedDistanceMeters / totalDistanceMeters
      );

      PointDto point = points.get(i);
      points.set(i, new PointDto(
          point.latitude(),
          point.longitude(),
          point.elevation(),
          firstPoint.time().plusMillis(elapsedMillis)
      ));
    }
  }
}
