package core;


import com.google.common.collect.ImmutableList;
import core.accessControllLayer.AccessController;
import core.accessControllLayer.ActionDescriptor;
import core.dataGateway.DataGateway;
import core.serviceEndPoints.ServiceEndPoint;

import java.util.List;
import java.util.function.Function;


public class Pipeline<IncomingType, OutgoingType, ResponseType, ActionType extends ActionDescriptor> {
    private final ServiceEndPoint serviceEndPoint;
    private final AccessController accessController;
    private final List<Function> incomingSerializationSteps;
    private final List<Function> outgoingSerializationSteps;
    private final DataGateway<OutgoingType, ResponseType, ActionType> dataGateway;
    private boolean started = false;



    Pipeline(ServiceEndPoint serviceEndPoint, AccessController accessController, DataGateway<OutgoingType, ResponseType, ActionType> dataGateway, List<Function> incomingSerializationSteps, List<Function> outgoingSerializationSteps){
        this.serviceEndPoint = serviceEndPoint;
        this.dataGateway = dataGateway;
        this.incomingSerializationSteps = ImmutableList.copyOf(incomingSerializationSteps);
        this.outgoingSerializationSteps = ImmutableList.copyOf(outgoingSerializationSteps);
        this.accessController = accessController;
    }

    public ResponseType authenticateAndExecuteAction(ActionType action, IncomingType content){
        if(accessController.authenticateAction(action)){
            return tranformAndProcessContent(action, content);
        }else{
            //TODO: authentication error handling
        }
        return null;
    }

    private ResponseType tranformAndProcessContent(ActionType action, Object content){
        Object tmpContent = content;
        for(Function transformer : this.incomingSerializationSteps){
            tmpContent = transformer.apply(tmpContent);
        }
        //there should be an assert here to check whether this cast is going to fail. but sadly it can't be done because of compiletime erasure of generics.
        try{
            final OutgoingType requestContent = (OutgoingType)tmpContent;
            Object tmpResponse = this.dataGateway.process(action, requestContent);
            for(Function transformer : this.outgoingSerializationSteps){
                tmpResponse = transformer.apply(tmpResponse);
            }

            return (ResponseType) tmpResponse;

        }catch(ClassCastException e){
            //TODO: Do correct logging here
            e.printStackTrace();
        }
        return null;
    }

    public void start(){
        if(!started){
            this.serviceEndPoint.start();
            this.dataGateway.start();
            this.started = true;
        }
    }

    public void shutdown(){
        if(started){
            this.serviceEndPoint.start();
            this.dataGateway.start();
        }
    }

}
