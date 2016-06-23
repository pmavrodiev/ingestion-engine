package impls.KafkaREST;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import core.Pipeline;
import core.serviceEndPoints.ServiceEndPoint;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static spark.Spark.*;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class KafkaRESTServiceEndpoint implements ServiceEndPoint {
    private Pipeline<Request, ?, String, KafkaRESTAction> pipeline;
    private final KafkaRESTAppender appender;
    private final Gson gson;

    private final static String DEFAULT_RESPONSE_VALUE = "{}";



    public KafkaRESTServiceEndpoint(String bidningInterface, int port){
        port(port);
        ipAddress(bidningInterface);
        this.appender = new KafkaRESTAppender();
        this.gson = new Gson();
    }


    @Override
    public void start() {


        get("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.GET), request);
        });

        post("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.POST), request);
        });

        put("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.UPDATE), request);
        });

        delete("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.DELETE), request);
        });
    }

    private String processRequest(KafkaRESTAction action, Request request){
        final String kafkaResponseString = this.pipeline.authenticateAndExecuteAction(action, request, DEFAULT_RESPONSE_VALUE);

        JsonParser parser = new JsonParser();
        final JsonElement kafkaResponse = parser.parse(kafkaResponseString);

        final Boolean hasErrors = this.appender.isHasErrors();
        final List<JsonObject> logLines = this.appender.getLogLines();

        final KafkaRESTJsonResponse response = new KafkaRESTJsonResponse(kafkaResponse, logLines, hasErrors);

        return gson.toJson(response);

    }


    @Override
    public void shutdown() {
        stop();
    }

    @Override
    public void injectPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public Appender getClientFeedbackAppender() {
        return this.appender;
    }


}
