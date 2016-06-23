package core.serviceEndPoints;

import core.Pipeline;
import org.apache.log4j.Appender;
import org.apache.log4j.Priority;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public interface ServiceEndPoint {
    void start();
    void shutdown();
    void injectPipeline(Pipeline pipeline);
    Appender getClientFeedbackAppender();
}
