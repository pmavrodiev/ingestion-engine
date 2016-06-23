package impls.KafkaREST;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import core.dataGateway.DataGateway;

import java.io.IOException;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class KafkaRESTDataGateway implements DataGateway<KafkaRESTRequest, String, KafkaRESTAction> {
    private final String kafkaRestProxyURL;

    public KafkaRESTDataGateway(String kafkaRestProxyURL){
        this.kafkaRestProxyURL = kafkaRestProxyURL;
    }


    @Override
    public String process(KafkaRESTAction kafkaRestAction, KafkaRESTRequest content) {

        String result= "";
        try{
            switch(kafkaRestAction.getVerb()){
                case GET:
                    result = Unirest.get(kafkaRestProxyURL + content.getUrlAddition()).headers(content.getHeaders()).asString().getBody();
                    break;
                case POST:
                    result = Unirest.post(kafkaRestProxyURL + content.getUrlAddition()).headers(content.getHeaders()).body(content.getRequestBody()).asString().getBody();
                    break;
                case DELETE:
                    result = Unirest.delete(kafkaRestProxyURL + content.getUrlAddition()).headers(content.getHeaders()).body(content.getRequestBody()).asString().getBody();
                    break;
                case UPDATE:
                    result = Unirest.patch(kafkaRestProxyURL + content.getUrlAddition()).headers(content.getHeaders()).body(content.getRequestBody()).asString().getBody();
                    break;
            }
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void start() {

    }

    @Override
    public void shutdown() {
        try {
            Unirest.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
