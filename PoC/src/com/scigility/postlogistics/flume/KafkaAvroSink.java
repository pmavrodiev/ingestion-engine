/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.scigility.postlogistics.flume;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.conf.ConfigurationException;
import org.apache.flume.sink.AbstractSink;

import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaAvroSink extends AbstractSink implements Configurable {
    private static final Logger log = LoggerFactory.getLogger(KafkaAvroSink.class);
    private Producer<String, GenericRecord> producer;
    private Properties kafkaProducerProps = new Properties();
    private Properties kafkaProps = new Properties();
    protected Schema schema;

    public Status process() throws EventDeliveryException {
        Channel channel = getChannel();
        Transaction tx = channel.getTransaction();
        try {
            tx.begin();
            Event event = channel.take();
            if (event == null) {
                tx.commit();
                return Status.READY;

            }

            String line = new String (event.getBody());
            log.debug("Got data " + line);
            log.debug("Creating data record ...");
            GenericRecord map = KafkaAvroSinkUtil.parseMessage(kafkaProps,line, schema);
            String topic = kafkaProps.getProperty(KafkaAvroSinkUtil.REQUIRED_TOPIC);
            ProducerRecord<String, GenericRecord> data = new ProducerRecord<String, GenericRecord>(topic, map);
            log.debug("Sending data ...");
            producer.send(data);
            tx.commit();
            log.info("Transaction committed ...");
            return Status.READY;
        }
        catch (Throwable th) {
            try {
                //log.info("Rollback Exception: {}",e1);
                tx.rollback();
                //return Status.BACKOFF;
            }
            catch (Exception e) {
                log.error("Rollback Exception:{}", e);
            }
            log.error("KafkaAvroSink: Failed to commit transaction. Transaction rolled back.", th);
            if (th instanceof  Error || th instanceof RuntimeException) {
                //log.error("KafkaAvroSink: Failed to commit transaction. Transaction rolled back.", th);
                Throwables.propagate(th);
            } else {
                throw new EventDeliveryException(th);
            }
            return Status.BACKOFF;
        } finally {
            if (tx != null)
                tx.close();
        }
    }

    public void configure(Context context) {
        //org.apache.log4j.BasicConfigurator.configure();

        // Check for the required properties
        for (int i = 0; i < KafkaAvroSinkUtil.REQUIRED_PROPS.length; i++) {
            String prop = context.getString(KafkaAvroSinkUtil.REQUIRED_PROPS[i]);
            if (prop == null)
                throw new ConfigurationException(KafkaAvroSinkUtil.REQUIRED_PROPS[i] + " must be specified");
            else
                kafkaProps.put(KafkaAvroSinkUtil.REQUIRED_PROPS[i], prop);

        }
        for (int i = 0; i < KafkaAvroSinkUtil.REQUIRED_KAFKA_PROPS.length; i++) {
            String exists = context.getString(KafkaAvroSinkUtil.REQUIRED_KAFKA_PROPS[i]);
            if (exists == null)
                throw new ConfigurationException(KafkaAvroSinkUtil.REQUIRED_KAFKA_PROPS[i] + " must be specified");
            // note we cannot store these in kafkaProducerProps, yet, since they have the 'kafka.' prefix
        }

        // Get the other "kafka." props that are to be passed to the producer
        log.info("context={}", context.toString());
        Map<String, String> kafkaProperties = context.getSubProperties(KafkaAvroSinkUtil.PROPERTY_PREFIX);
        for (Map.Entry<String, String> prop : kafkaProperties.entrySet()) {
            kafkaProducerProps.put(prop.getKey(), prop.getValue());
            log.info("Reading a Kafka Producer Property: key: " + prop.getKey() + ", value: " + prop.getValue());
        }
        try {
            schema = new Schema.Parser().parse(new File(kafkaProps.getProperty(KafkaAvroSinkUtil.REQUIRED_AVRO_SCHEMA)));
        }
        catch (IOException e) {
            log.error(e.toString());
        }
        producer = new KafkaProducer<String, GenericRecord>(kafkaProducerProps);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public synchronized void stop() {
        producer.close();
        super.stop();
    }
}