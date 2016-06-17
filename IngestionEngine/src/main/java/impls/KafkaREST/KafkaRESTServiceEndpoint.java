package impls.KafkaREST;

import core.Pipeline;
import core.serviceEndPoints.ServiceEndPoint;
import spark.Request;

import static spark.Spark.*;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class KafkaRESTServiceEndpoint implements ServiceEndPoint {
    private Pipeline<Request, ?, String, KafkaRESTAction> pipeline;

    public KafkaRESTServiceEndpoint(String bidningInterface, int port){
        port(port);
        ipAddress(bidningInterface);
    }


    @Override
    public void start() {

        get("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.GET), request);
        });

        post("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.GET), request);
        });

        put("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.GET), request);
        });

        delete("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.GET), request);
        });
    }

    private String processRequest(KafkaRESTAction action, Request request){
        try{
            return this.pipeline.authenticateAndExecuteAction(action, request);
        }catch(Throwable t){
            t.printStackTrace();
        }
        return "";
    }


    @Override
    public void shutdown() {
        stop();
    }

    @Override
    public void injectPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }
}
