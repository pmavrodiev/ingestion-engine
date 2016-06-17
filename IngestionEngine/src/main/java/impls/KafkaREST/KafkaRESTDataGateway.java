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
        switch(kafkaRestAction.getVerb()){
            case GET:
                try {
                    result = Unirest.get(kafkaRestProxyURL + content.getUrlAddition()).asString().getBody();
                } catch (UnirestException e) {
                    throw new RuntimeException(e);
                }
                break;
            case POST:
                Unirest.post(kafkaRestProxyURL + content.getUrlAddition()).body(content.getRequestBody());
                break;
            case DELETE:
                Unirest.delete(kafkaRestProxyURL + content.getUrlAddition()).body(content.getRequestBody());
                break;
            case UPDATE:
                //?
                break;
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
