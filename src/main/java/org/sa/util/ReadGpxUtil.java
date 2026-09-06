package org.sa.util;
import org.sa.PointDto;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ReadGpxUtil {

  public static List<PointDto> read(String path) throws Exception {
    var document = DocumentBuilderFactory.newInstance()
        .newDocumentBuilder()
        .parse(Path.of(path).toFile());

    var points = document.getElementsByTagName("trkpt");

    return new ArrayList<>(IntStream.range(0, points.getLength())
        .mapToObj(points::item)
        .map(node -> toPoint((Element) node))
        .toList());
  }

  private static PointDto toPoint(Element point) {
    return new PointDto(
        Double.parseDouble(point.getAttribute("lat")),
        Double.parseDouble(point.getAttribute("lon")),
        Double.parseDouble(point.getElementsByTagName("ele").item(0).getTextContent()),
        Instant.parse(point.getElementsByTagName("time").item(0).getTextContent())
    );
  }
}
