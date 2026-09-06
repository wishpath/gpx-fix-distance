package org.sa;

import java.time.Instant;

public record PointDto(
    double latitude,
    double longitude,
    double elevation,
    Instant time
) {
}
