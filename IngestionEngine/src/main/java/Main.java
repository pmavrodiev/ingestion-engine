import core.Pipeline;
import core.PipelineBuilder;
import core.accessControllLayer.DummyAccessController;
import impls.KafkaREST.KafkaRESTDataGateway;
import impls.KafkaREST.KafkaRESTServiceEndpoint;
import impls.KafkaREST.KafkaRESTAction;
import impls.KafkaREST.KafkaRESTRequest;
import spark.Request;

import java.util.function.Function;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class Main {

    public static void main(String[] args){
        Function<Request, KafkaRESTRequest> transformer = KafkaRESTRequest::fromRequest;

        @SuppressWarnings(value = "unchecked")
        Pipeline<Request,KafkaRESTRequest, String, KafkaRESTAction> pipeline =
                new PipelineBuilder<Request,KafkaRESTRequest, String, KafkaRESTAction>()
                        .setServiceEndPoint(new KafkaRESTServiceEndpoint("127.0.0.1", 8081))
                        .setAccessController(new DummyAccessController())
                        .setDataGateway(new KafkaRESTDataGateway("127.0.0.1:8080"))
                        .<Request, KafkaRESTRequest>addIncomingSerializationStep(transformer) //If you use IntelliJ Ignore the "Type Arguments given on a raw Method" error....that's an IntelliJ bug
                        .build();

        pipeline.start();

    }


}
