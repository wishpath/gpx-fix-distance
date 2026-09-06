package org.sa.util;

import org.sa.PointDto;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class WriteGpxUtil {

  public static void createGpxFile(List<PointDto> points, String inputFilePath, String outputDirectoryPath) throws Exception {
    String originalFileName = Path.of(inputFilePath).getFileName().toString();
    String originalName = originalFileName.substring(0, originalFileName.length() - ".gpx".length());
    Path outputPath = Path.of(outputDirectoryPath, originalName + "_FIXED.gpx");
    Files.createDirectories(outputPath.getParent());
    Files.writeString(outputPath, createGpxString(points, originalName));
  }

  private static String createGpxString(List<PointDto> points, String name) {
    return """
        <?xml version="1.0" encoding="UTF-8"?>
        <gpx version="1.1" creator="gps-fix"
             xmlns="http://www.topografix.com/GPX/1/1">
          <trk>
            <name>%s</name>
            <trkseg>
        %s
            </trkseg>
          </trk>
        </gpx>
        """.formatted(name, points.stream().map(WriteGpxUtil::toGpxPoint).reduce("", String::concat));
  }

  private static String toGpxPoint(PointDto point) {
    return """
                <trkpt lat="%.7f" lon="%.7f">
                  <ele>%.2f</ele>
                  <time>%s</time>
                </trkpt>
        """.formatted(point.latitude(), point.longitude(), point.elevation(), point.time());
  }
}