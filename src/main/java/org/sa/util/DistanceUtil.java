package org.sa.util;

import org.sa.PointDto;

import java.util.List;
import java.util.stream.IntStream;

public class DistanceUtil {

  private static final double EARTH_RADIUS_METERS = 6_371_000;
  private static final int METERS_IN_KM = 1000;

  public static double measureDistanceMeters(PointDto first, PointDto second) {
    var lat1 = Math.toRadians(first.latitude());
    var lat2 = Math.toRadians(second.latitude());
    var deltaLat = lat2 - lat1;
    var deltaLon = Math.toRadians(second.longitude() - first.longitude());

    var a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
        + Math.cos(lat1) * Math.cos(lat2)
        * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_METERS * c;
  }

  public static double measureDistanceKilometers(PointDto first, PointDto second) {
     return measureDistanceMeters(first, second) / METERS_IN_KM;
  }

  public static double measureTotalDistanceMeters(List<PointDto> points) {
    return IntStream.range(1, points.size()).mapToDouble(i -> DistanceUtil.measureDistanceMeters(points.get(i - 1), points.get(i))).sum();
  }

  public static void addPointWithMissingDistance(List<PointDto> points, double missingDistanceMeters) {
    double directionInRadians = calculateDirectionInRadians(points.get(points.size() - 2), points.getLast());
    PointDto newPoint = createPoint(points.getLast(), directionInRadians, missingDistanceMeters);
    points.add(newPoint);
  }

  private static double calculateDirectionInRadians(PointDto first, PointDto second) {
    double lat1 = Math.toRadians(first.latitude());
    double lat2 = Math.toRadians(second.latitude());
    double deltaLon = Math.toRadians(second.longitude() - first.longitude());
    double y = Math.sin(deltaLon) * Math.cos(lat2);
    double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(deltaLon);
    return Math.atan2(y, x);
  }

  private static PointDto createPoint(PointDto point, double directionInRadians, double distanceMeters) {
    double angularDistance = distanceMeters / EARTH_RADIUS_METERS;
    double latitude = Math.toRadians(point.latitude());
    double longitude = Math.toRadians(point.longitude());
    double newLatitude = Math.asin(Math.sin(latitude) * Math.cos(angularDistance) + Math.cos(latitude) * Math.sin(angularDistance) * Math.cos(directionInRadians));
    double newLongitude = longitude + Math.atan2(Math.sin(directionInRadians) * Math.sin(angularDistance) * Math.cos(latitude), Math.cos(angularDistance) - Math.sin(latitude) * Math.sin(newLatitude));
    return new PointDto(Math.toDegrees(newLatitude), Math.toDegrees(newLongitude), point.elevation(), point.time().plusSeconds(1));
  }
}
