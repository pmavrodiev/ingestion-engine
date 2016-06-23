package impls.KafkaREST;

import com.google.common.collect.Maps;
import spark.Request;

import java.util.Map;
import java.util.Set;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class KafkaRESTRequest {

    private String urlAddition;
    private String requestBody;
    private Map<String,String> headers;

    private KafkaRESTRequest(){}

    public static KafkaRESTRequest fromRequest(Request request){
        KafkaRESTRequest restRequest = new KafkaRESTRequest();

        restRequest.headers = Maps.newHashMap();
        restRequest.headers.put("Content-Type", request.contentType());
        restRequest.urlAddition = request.uri();
        restRequest.requestBody = request.body();

        return restRequest;
    }

    public String getUrlAddition() {
        return urlAddition;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public Map<String,String> getHeaders() {
        return headers;
    }
}
