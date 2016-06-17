package impls.KafkaREST;

import spark.Request;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class KafkaRESTRequest {

    private String urlAddition;
    private String requestBody;

    private KafkaRESTRequest(){}

    public static KafkaRESTRequest fromRequest(Request request){
        KafkaRESTRequest restRequest = new KafkaRESTRequest();

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
}
