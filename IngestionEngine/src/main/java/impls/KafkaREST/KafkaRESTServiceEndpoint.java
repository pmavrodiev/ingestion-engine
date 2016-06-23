package impls.KafkaREST;

import com.google.common.collect.Lists;
import com.google.gson.*;
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



    public KafkaRESTServiceEndpoint(String bindingInterface, int port){
        port(port);
        ipAddress(bindingInterface);
        this.appender = new KafkaRESTAppender();
        this.gson = new Gson();
    }


    @Override
    public void start() {

        get("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.GET), request, response);
        });

        post("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.POST), request, response);
        });

        put("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.UPDATE), request, response);
        });

        delete("/*", (request, response) -> {
            return processRequest(new KafkaRESTAction(KafkaRESTAction.Verb.DELETE), request, response);
        });
    }

    private String processRequest(KafkaRESTAction action, Request request, Response response){
        response.header("Content-Type", "application/json");

        final String kafkaResponseString = this.pipeline.authenticateAndExecuteAction(action, request, DEFAULT_RESPONSE_VALUE);
        Boolean wrapped;

        JsonParser parser = new JsonParser();
        JsonElement kafkaResponse;
        try{
            kafkaResponse = parser.parse(kafkaResponseString);
            wrapped = false;
        }catch(JsonSyntaxException exception){ //some responses from the Kafka rest proxy themselves are not valid json
            kafkaResponse = parser.parse("{wrappedResponse: \""+kafkaResponseString+"\"}");
            wrapped = true;
        }

        final Boolean hasErrors = this.appender.isHasErrors();
        final List<JsonObject> logLines = this.appender.getLogLines();

        final KafkaRESTJsonResponse responseContent = new KafkaRESTJsonResponse(kafkaResponse, logLines, hasErrors, wrapped);

        this.appender.reset();

        return gson.toJson(responseContent);

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
