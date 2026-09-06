# GPS Fix

Add a few missing meters to your GPS track and even out pace.

## What it does

* Writes a new `_FIXED.gpx` file with these changes.
  * Adds up to 500m to a track.
  * Sets the target duration
  * Sets even pace throughout the track.

## Files eligible for fixing

* GPX files with slightly **missing** distance (missing distance must be 500m or less)
* GPX files with **mostly** corrupted split times (outside pace range of 3-9min/km).
* First point start time must match the configuration in `Main.java`.


## Usage

1. Put your `.gpx` file in the input folder.
2. Configure `Main.java`:
    * target distance
    * target start time
    * target duration
    * input file path
3. Run `Main`.

### Requirements

* Java 21+

Output is created automatically in the output folder:
```text
my_activity.gpx
→ my_activity_FIXED.gpx
```
