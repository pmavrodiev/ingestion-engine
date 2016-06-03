package com.scigility.postlogistics.flume;


import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.errors.SerializationException;

import java.text.ParseException;
import java.util.Scanner;

/**
 * Created by pmavrodiev on 08/05/16.
 */
public class CrashAuswertungParser implements Parser {
    @Override
    public GenericRecord init(String line, Schema schema) {
        GenericRecord map = new GenericData.Record(schema);

        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\\|");

        String[] schemaKeys = {"Datum", "Traysorter", "Nummern_CHO1", "Nummern_CHO2", "Nummern_CHO3", "Nummern_CHO4", "Nummern_CHO5",
                               "Vor_Crash_CHO_neues_Fahrwerk", "Vor_Crash_CHO_altes_Fahrwerk","Nach_Crash_CHO_neues_Farhwerk",
        "Nach_Crash_CHO_altes_Fahrwerk","Vor_Crash_CHO_neuer_Kippmechanismus","Vor_Crash_CHO_alter_Kippmechanismus",
        "Nach_Crash_CHO_neuer_Kippmechanismus","Nach_Crash_CHO_alter_Kippmechanismus","Crashstandort",
        "Paketgut","Element_CHO_Kippmechanismus","Element_CHO_Fahrwerk","Element_CHO_Hebel","Verteillzentrum"};


        log.info("Parsing : " + line);

        int counter = 0;

        try {
            while (scanner.hasNext() && counter < schemaKeys.length)  {
                String itemValue = scanner.next();
                if (itemValue.isEmpty()) {
                    /*
                     * Add an empty string for the string fields instead of null as prescribed by the Avro schema.
                     * The issue is that the Confluent HDFS connector does not check for null strings.
                     * It tries to get the class of the provided item by doing value.getClass(), which will
                     * give a NullPointer exception when value=null.
                     * See this for details: https://github.com/confluentinc/schema-registry/issues/272
                    */
                    map.put(schemaKeys[counter],"");
                }
                else
                    map.put(schemaKeys[counter], itemValue);
                counter++;
            }
        }
        catch (SerializationException e) {
            log.error(e.toString());
        }
        scanner.close();

        return map;
    }

}

