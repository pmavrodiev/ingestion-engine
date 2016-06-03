# File structure

## src/

Main classes for the custom Flume sink

- **KafkaAvroSink.java**
- **KafkaAvroSinkUtil.java**

## lib/

Required libraries to be either packed into the final jar or found in the classpath

## logs/

Post-processed log files provided by Postlogistics for the PoC. The original Excel log files were converted to csv (with `|` as separator)

Postlogistics gave us 3 log files. Two of them (HAEBDK) were intended to showcase schema evolution. The third one (CRASH) had a more complex structure, but we showed how it can, nevertheless, be turned into an Avro schema

- **HAEBDK/OLD/Fehlverhalten HAEBDK_OLD.csv** - This represents a log file with an initial schema

- **HAEBDK/NEU/Fehlverhalten HAEBDK.neu** - This represents a log file with a new schema having more fields compared to Fehlverhalten HAEBDK_OLD.csv

- **HAEBDK/BAD/Fehlverhalten HAEBDK.neu** - This is a not a separate log file per se (same as the previous one), but rather used to demonstrate that the schema registry will reject input that does not conform to an expected schema. The file is actually "good", what is bad is the schema we will associate with it. We showcased this functionality as a last stage of the PoC. For esthetic reasons, the inputs to this stage (input files, Flume configuration files, etc.) are stored in separate directory structures.

- **CRASH/CrashAuswertung.csv** - A more complex log file that appeared to have manual structure. We still created a schema out of it.

## res/
Contains the schemas for the log files above. Notice that the only difference between Fehlverhalten_HAEBDK_avro_BAD.json and Fehlverhalten_HAEBDK_avro_NEU.json is in the Datum field. The "BAD" version expects a string while the input will provide a long.

## conf/

Flume configuration files for processing the different log files

- **flume_OLD.conf**, **flume_NEU.conf** and flume_CRASH.conf  - Use this conf for Fehlverhalten HAEBDK_OLD.csv, Fehlverhalten HAEBDK.neu and CrashAuswertung.csv, respectively.

- **flume_BAD.conf** - Almost the same as flume_NEU.conf except we specify a different schema.

## flume-plugins/

The custom Flume sink is compiled and packed with all required libraries into a jar. Flume can read a plug-in directory for such custom components, which must have a predefined structure. This is the plugin-directory.

## scripts/

  Utility scripts what are self-explanatory. Probably the most relevant ones are run-flume-*.sh, which should be used to start flume with the proper configuration.

## out/

  This is IntelliJ specific. The compiled project and packaged jar are put here in production/ and artifacts/ respectively.
