import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.utils.MapUtil;
import core.Pipeline;
import core.PipelineBuilder;
import core.accessControllLayer.DummyAccessController;
import impls.KafkaREST.KafkaRESTAction;
import impls.KafkaREST.KafkaRESTDataGateway;
import impls.KafkaREST.KafkaRESTRequest;
import impls.KafkaREST.KafkaRESTServiceEndpoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import spark.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;

/**
 * @author Graf_Blutwurst
 * @Date 6/23/2016
 */
public class MainTest {
    private Pipeline pipeline;

    private static final class ValueHolder{
        private Integer value;

        public ValueHolder(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    private static final class AVRORequest{
        private String value_schema;
        private List<ValueHolder> records;

        public AVRORequest(String value_schema, List<ValueHolder> records) {
            this.value_schema = value_schema;
            this.records = records;
        }

        public String getValue_schema() {
            return value_schema;
        }

        public void setValue_schema(String value_schema) {
            this.value_schema = value_schema;
        }

        public List<ValueHolder> getRecords() {
            return records;
        }

        public void setRecords(List<ValueHolder> records) {
            this.records = records;
        }
    }

    @Before
    public void setUp() throws Exception {

        this.pipeline =
                new PipelineBuilder<Request,KafkaRESTRequest, String, KafkaRESTAction>()
                        .setServiceEndPoint(new KafkaRESTServiceEndpoint("127.0.0.1", 8081))
                        .setAccessController(new DummyAccessController(true))
                        .setDataGateway(new KafkaRESTDataGateway("http://127.0.0.1:8082"))
                        .addIncomingSerializationStep( (Function<Request, KafkaRESTRequest>)KafkaRESTRequest::fromRequest ) //If you use IntelliJ Ignore the "Type Arguments given on a raw Method" error....that's an IntelliJ bug
                        .build();

        pipeline.start();

    }

    @After
    public void tearDown() throws Exception {
        pipeline.shutdown();
    }

    @Test
    public void mainPOSTTest() throws Exception {
        final String url = "http://localhost:8081/topics/testabc";
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/vnd.kafka.avro.v1+json");
        headers.put("Accept", "application/vnd.kafka.v1+json, application/vnd.kafka+json, application/json");
        final ValueHolder valueHolder = new ValueHolder(12);
        final AVRORequest requestBody = new AVRORequest("{\"name\":\"int\",\"type\": \"int\"}", Lists.newArrayList(valueHolder));
        final Gson gson = new Gson();
        final String json = gson.toJson(requestBody);
        System.out.println(json);


        String result = Unirest.post(url).headers(headers).body(json).asString().getBody();
        System.out.println(result);

        //that would indicate that the POST was a success. No response from the Kafka Proxy should start like this if there was an error
        //and if there was an internal proxy error (e.g. auth) the kafkaRestProxyResponse will be {} thus failing
        Boolean testFlag = result.startsWith("{\"kafkaRESTProxyResponse\":{\"offsets\"");

        assertNotEquals(result, testFlag);
    }

}