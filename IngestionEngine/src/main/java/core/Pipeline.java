package core;


import com.google.common.collect.ImmutableList;
import core.accessControllLayer.AccessController;
import core.accessControllLayer.ActionDescriptor;
import core.accessControllLayer.AuthenticationResult;
import core.dataGateway.DataGateway;
import core.serviceEndPoints.ServiceEndPoint;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.function.Function;


public class Pipeline<IncomingType, DataGatewayExpectedType, DataGatewayResponseType, ActionType extends ActionDescriptor> {
    private final static String CLIENT_FEEDBACK_LOGGER_NAME = "Client Feedback";

    private final static Logger internalLogger = Logger.getLogger(Pipeline.class);

    private final ServiceEndPoint serviceEndPoint;
    private final AccessController accessController;
    private final List<Function> incomingSerializationSteps;
    private final List<Function> outgoingSerializationSteps;
    private final DataGateway<DataGatewayExpectedType, DataGatewayResponseType, ActionType> dataGateway;
    private boolean started = false;

    private Logger clientFeedbackLogger;



    Pipeline(ServiceEndPoint serviceEndPoint, AccessController accessController, DataGateway<DataGatewayExpectedType, DataGatewayResponseType, ActionType> dataGateway, List<Function> incomingSerializationSteps, List<Function> outgoingSerializationSteps){
        this.serviceEndPoint = serviceEndPoint;
        this.dataGateway = dataGateway;
        this.incomingSerializationSteps = ImmutableList.copyOf(incomingSerializationSteps);
        this.outgoingSerializationSteps = ImmutableList.copyOf(outgoingSerializationSteps);
        this.accessController = accessController;
    }

    public DataGatewayResponseType authenticateAndExecuteAction(ActionType action, IncomingType content, DataGatewayResponseType defaultValue){
        final AuthenticationResult authResult = accessController.authenticateAction(action);
        if(authResult.getSuccess()){
            return tranformAndProcessContent(action, content);
        }else{
            clientFeedbackLogger.log(Level.ERROR, authResult.getAuthenticationMessage());
        }
        return defaultValue;
    }

    private DataGatewayResponseType tranformAndProcessContent(ActionType action, Object content){
        Object tmpContent = content;
        for(Function transformer : this.incomingSerializationSteps){
            tmpContent = transformer.apply(tmpContent);
        }
        //there should be an assert here to check whether this cast is going to fail. but sadly it can't be done because of compiletime erasure of generics.
        try{
            final DataGatewayExpectedType requestContent = (DataGatewayExpectedType)tmpContent;
            Object tmpResponse = this.dataGateway.process(action, requestContent);
            for(Function transformer : this.outgoingSerializationSteps){
                tmpResponse = transformer.apply(tmpResponse);
            }

            return (DataGatewayResponseType) tmpResponse;

        }catch(ClassCastException e){
            internalLogger.log(Level.FATAL, "Incompatible Transformer chain. Check Pipeline config");
            internalLogger.log(Level.FATAL, e);
            clientFeedbackLogger.log(Level.FATAL, "Internal Server Error");
        }
        return null;
    }

    public void start(){
        if(!started){
            this.serviceEndPoint.start();
            this.dataGateway.start();
            setUpClientFeedbackLogger();
            this.started = true;
        }
    }

    public void shutdown(){
        if(started){
            this.serviceEndPoint.shutdown();
            this.dataGateway.shutdown();
        }
    }

    private void setUpClientFeedbackLogger(){
        this.clientFeedbackLogger = Logger.getLogger(CLIENT_FEEDBACK_LOGGER_NAME);
        Appender clientAppender = this.serviceEndPoint.getClientFeedbackAppender();

        if(clientAppender == null){
            throw new IllegalStateException("Service Endpoint has provided null as a client Appender");
        }

        this.clientFeedbackLogger.addAppender(clientAppender);
    }

}
