package core.dataGateway;

import core.accessControllLayer.ActionDescriptor;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public interface DataGateway<ExpectedContentType, ResponseType, ActionType extends ActionDescriptor> {
    ResponseType process( ActionType type, ExpectedContentType content);
    void start();
    void shutdown();
}
