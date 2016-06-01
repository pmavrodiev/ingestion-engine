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


import java.util.Properties;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class KafkaAvroSinkUtil {
    private static final Logger log = LoggerFactory.getLogger(KafkaAvroSinkUtil.class);

    protected static final String REQUIRED_PARSER_CLASS = "parser.class";
    protected static final String REQUIRED_AVRO_SCHEMA = "avro.schema.file";
    protected static final String REQUIRED_TOPIC = "topic";

    protected static final String[] REQUIRED_PROPS = {REQUIRED_PARSER_CLASS, REQUIRED_AVRO_SCHEMA, REQUIRED_TOPIC};

    protected static final String PROPERTY_PREFIX = "kafka.";

    protected static final String REQUIRED_KAFKA_BOOTSTRAP_SERVERS = "kafka.bootstrap.servers";
    protected static final String REQUIRED_KAFKA_KEY_SERIALIZER = "kafka.key.serializer";
    protected static final String REQUIRED_KAFKA_VALUE_SERIALIZER = "kafka.value.serializer";
    protected static final String REQUIRED_KAFKA_SCHEMA_REG_URL = "kafka.schema.registry.url";


    protected static final String[] REQUIRED_KAFKA_PROPS = {REQUIRED_KAFKA_BOOTSTRAP_SERVERS,
                                                            REQUIRED_KAFKA_VALUE_SERIALIZER,
                                                            REQUIRED_KAFKA_KEY_SERIALIZER,
                                                            REQUIRED_KAFKA_SCHEMA_REG_URL};


    public static GenericRecord parseMessage(Properties props, String line, Schema schema) {
        try {
            Parser parser = (Parser) Class.forName(props.getProperty(REQUIRED_PARSER_CLASS)).newInstance();
            return parser.init(line, schema);
        }
        catch (Exception e) {
            log.error("KafkaAvroUtilSink Exception:{}", e);
            return null;
        }

    }





}

















