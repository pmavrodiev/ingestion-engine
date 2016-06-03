package com.scigility.postlogistics.flume;


import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.Schema;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import java.util.Scanner;
import org.apache.kafka.common.errors.SerializationException;

/**
 * Created by pmavrodiev on 08/05/16.
 */
public class HaebdkParser_NEU implements Parser {
    @Override
    public GenericRecord init(String line, Schema schema) {
        GenericRecord map = new GenericData.Record(schema);

        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\\|");

        String[] schemaKeys = {"NR", "Datum", "EventNr", "Fehler", "Name", "Anmerkung", "InArbeitBei",
                               "ErledigtDatum", "Kommentar"};


        log.info("Parsing : " + line);

        DateFormat dfm = new SimpleDateFormat("dd/MM/yy");
        dfm.setLenient(false);

        int counter = 0;

        try {
            while (scanner.hasNext()) {
                String itemValue = scanner.next();
                if (!itemValue.isEmpty()) {
                    if (counter == 1 /* Datum */|| counter == 7 /* ErledigtDatum */) {
                        try {
                            long timestamp = dfm.parse(itemValue).getTime();
                            map.put(schemaKeys[counter], timestamp);
                        }
                        catch (ParseException e) {
                            log.info("Parser Exception:{}", e);
                        }
                    }
                    else if (counter == 0 /* NR */) {
                        int nr = Integer.parseInt(itemValue);
                        map.put(schemaKeys[counter], nr);
                    }
                    else // the rest are strings
                        map.put(schemaKeys[counter], itemValue);
                }
                else {
                    if (counter == 2 || counter == 3 || counter == 4 || counter == 5 || counter == 6 || counter == 8)
                        /*
                         * Add an empty string for the string fields instead of null as prescribed by the Avro schema.
                         * The issue is that the Confluent HDFS connector does not check for null strings.
                         * It tries to get the class of the provided item by doing value.getClass(), which will
                         * give a NullPointer exception when value=null.
                         * See this for details: https://github.com/confluentinc/schema-registry/issues/272
                        */
                        map.put(schemaKeys[counter],"");
                    else if (counter == 7 /* ErledigtDatum */)
                        map.put(schemaKeys[counter], (long) -1);
                }
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

