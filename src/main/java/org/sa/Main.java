package org.sa;

import org.sa.util.*;

import java.time.Duration;
import java.util.List;

public class Main {
  private static final double TARGET_DISTANCE_METERS = 21_97.5;
  private static final Duration TARGET_DURATION = Duration.ofMinutes(88).plusSeconds(18);
  private static final String FILE_PATH_INPUT = "src/main/java/org/sa/storage/input/half_marathon_Vilnius_Marathon_2013_PB.gpx";
  private static final String DIRECTORY_PATH_OUTPUT = "src/main/java/org/sa/storage/output/";

  public static void main(String[] args) throws Exception {

    // 1. get current total distance
    List<PointDto> points = ReadGpxUtil.read(FILE_PATH_INPUT);
    double deFactoDistanceMeters = DistanceUtil.measureTotalDistanceMeters(points);
    double missingDistanceMeters = TARGET_DISTANCE_METERS - deFactoDistanceMeters;

    // 2. do checks
    if (deFactoDistanceMeters > TARGET_DISTANCE_METERS) throw new IllegalArgumentException("App fixes gps tracks that are too short");
    if (!PaceUtil.isPaceTotallyOffForMostPoints(points)) throw new IllegalArgumentException("App fixes gps tracks that has pace totally of between most points");
    if (missingDistanceMeters > 500) throw new IllegalArgumentException("App fixes gps tracks that has missing distance up to 500 meters");

    // 3. add missing distance
    DistanceUtil.addPointWithMissingDistance(points, missingDistanceMeters);

    // 4. apply new timestamps
    PaceUtil.evenOutPaceBetweenEachPoints(points, TARGET_DURATION);
    PrintUtil.printPointsDetails(points);

    // 5. output new gpx file
    WriteGpxUtil.createGpxFile(points, FILE_PATH_INPUT, DIRECTORY_PATH_OUTPUT);
  }
}