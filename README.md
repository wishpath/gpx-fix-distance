# gps-fix-distance

## What it does
* Adds up to 500m to a track.
* Sets even pace throughout the track (of target duration)
* Output new file with suffix of `_FIXED.gpx`

## Input
* Original GPX file (variable `FILE_PATH_INPUT`)
* Variable `TARGET_DISTANCE_METERS`
* Variable `TARGET_DURATION`

## Usage
1. Put your `.gpx` file in the input folder.
2. Configure `Main.java`:
    * target distance
    * target duration
    * input file path
3. Run `Main`.

### Requirements
* Java 21+
