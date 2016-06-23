package core.dataGateway;

import core.accessControllLayer.ActionDescriptor;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public interface DataGateway<DataGatewayExpectedType, DataGatewayResponseType, ActionType extends ActionDescriptor> {
    DataGatewayResponseType process(ActionType type, DataGatewayExpectedType content);
    void start();
    void shutdown();
}
