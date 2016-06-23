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
    private boolean hasErrors;

    public KafkaRESTJsonResponse(Object kafkaRESTProxyResponse, List<JsonObject> logLines, boolean hasErrors) {
        this.kafkaRESTProxyResponse = kafkaRESTProxyResponse;
        this.logLines = logLines;
        this.hasErrors = hasErrors;
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

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }
}
