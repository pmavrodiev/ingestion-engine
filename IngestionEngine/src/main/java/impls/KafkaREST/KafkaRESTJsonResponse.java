package impls.KafkaREST;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * @author Graf_Blutwurst
 * @Date 6/23/2016
 */
public class KafkaRESTJsonResponse {
    private Object kafkaRESTProxyResponse;
    private List<JsonObject> logLines;
    private boolean hasProxyServerErrors;
    private boolean wrappedResponse;

    public KafkaRESTJsonResponse(Object kafkaRESTProxyResponse, List<JsonObject> logLines, boolean hasProxyServerErrors, boolean wrappedResponse) {
        this.kafkaRESTProxyResponse = kafkaRESTProxyResponse;
        this.logLines = logLines;
        this.hasProxyServerErrors = hasProxyServerErrors;
        this.wrappedResponse = wrappedResponse;
    }

    public Object getKafkaRESTProxyResponse() {
        return kafkaRESTProxyResponse;
    }

    public void setKafkaRESTProxyResponse(Object kafkaRESTProxyResponse) {
        this.kafkaRESTProxyResponse = kafkaRESTProxyResponse;
    }

    public List<JsonObject> getLogLines() {
        return logLines;
    }

    public void setLogLines(List<JsonObject> logLines) {
        this.logLines = logLines;
    }

    public boolean isHasProxyServerErrors() {
        return hasProxyServerErrors;
    }

    public void setHasProxyServerErrors(boolean hasProxyServerErrors) {
        this.hasProxyServerErrors = hasProxyServerErrors;
    }

    public boolean isWrappedResponse() {
        return wrappedResponse;
    }

    public void setWrappedResponse(boolean wrappedResponse) {
        this.wrappedResponse = wrappedResponse;
    }
}
