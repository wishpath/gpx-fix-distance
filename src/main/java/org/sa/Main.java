package org.sa;

import org.sa.util.*;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class Main {
  private static final double HALF_MARATHON_DISTANCE_METERS = 21_097.5;
  private static final double TARGET_DISTANCE = HALF_MARATHON_DISTANCE_METERS;
  private static final Instant TARGET_START_TIME = Instant.parse("2013-09-15T06:00:00Z"); //Vilnius time is +3h
  private static final Duration TARGET_DURATION = Duration.ofMinutes(88).plusSeconds(18);
  private static final String FILE_PATH_INPUT = "src/main/java/org/sa/storage/input/half_marathon_Vilnius_Marathon_2013_PB.gpx";
  private static final String DIRECTORY_PATH_OUTPUT = "src/main/java/org/sa/storage/output/";

  public static void main(String[] args) throws Exception {
    List<PointDto> points = ReadGpxUtil.read(FILE_PATH_INPUT);
    double deFactoDistanceMeters = DistanceUtil.measureTotalDistanceMeters(points);
    if (deFactoDistanceMeters > TARGET_DISTANCE) throw new IllegalArgumentException("App fixes gps tracks that are too short");
    if (!PaceUtil.isPaceTotallyOffForMostPoints(points)) throw new IllegalArgumentException("App fixes gps tracks that has pace totally of between most points");
    double missingDistanceMeters = TARGET_DISTANCE - deFactoDistanceMeters;
    if (missingDistanceMeters > 500) throw new IllegalArgumentException("App fixes gps tracks that has missing distance up to 500 meters");
    DistanceUtil.addPointWithMissingDistance(points, missingDistanceMeters);
    PaceUtil.evenOutPaceBetweenEachPoints(points, TARGET_DURATION);
    PrintUtil.printPointsDetails(points);
    WriteGpxUtil.createGpxFile(points, FILE_PATH_INPUT, DIRECTORY_PATH_OUTPUT);
  }
}