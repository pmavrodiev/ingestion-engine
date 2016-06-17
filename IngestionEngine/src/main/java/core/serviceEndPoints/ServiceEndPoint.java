package core.serviceEndPoints;

import core.Pipeline;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public interface ServiceEndPoint {
    void start();
    void shutdown();
    void injectPipeline(Pipeline pipeline);
}
