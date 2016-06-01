package com.scigility.postlogistics.flume;


import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.errors.SerializationException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

/**
 * Created by pmavrodiev on 08/05/16.
 */
public class HaebdkParser_OLD implements Parser {
    @Override
    public GenericRecord init(String line, Schema schema) {
        GenericRecord map = new GenericData.Record(schema);

        Scanner scanner = new Scanner(line);
        scanner.useDelimiter("\\|");

        String[] schemaKeys = {"Datum", "EventNr", "Fehler", "Name", "Anmerkung", "ErledigtDatum"};
        log.info("Parsing : " + line);

        DateFormat dfm = new SimpleDateFormat("dd/MM/yy");
        dfm.setLenient(false);

        int counter = 0;

        try {
            while (scanner.hasNext()) {
                String itemValue = scanner.next();
                if (!itemValue.isEmpty()) {
                    if (counter == 0 || counter == 5) {
                        try {
                            long timestamp = dfm.parse(itemValue).getTime();
                            map.put(schemaKeys[counter], timestamp);
                        }
                        catch (ParseException e) {
                            log.info("Parser Exception:{}", e);
                        }
                        // these are dates in format dd/mm/yy. convert to timestamps
                    }
                    else // the rest are strings
                        map.put(schemaKeys[counter], itemValue);
                }
                else {
                    if (counter == 1 || counter == 2 || counter == 3 || counter == 4)
                        map.put(schemaKeys[counter],"");
                    else if (counter == 5)
                        map.put(schemaKeys[counter], (long) -1);

                }
                //else {
                //    if (counter == 5) //ErledigtDatum
                 //       map.put(schemaKeys[counter], (long) -1);
                 //   else
                 //       map.put(schemaKeys[counter],"");
               // }
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

