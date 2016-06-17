package impls.KafkaREST;

import com.mashape.unirest.http.Unirest;
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
                result = Unirest.get(kafkaRestProxyURL + content.getUrlAddition()).toString();
                break;
            case POST:
                //Unirest.post();
                break;
            case DELETE:
                //Unirest.delete();
                break;
            case UPDATE:

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
