package core;

import com.google.common.collect.Lists;
import core.accessControllLayer.AccessController;
import core.accessControllLayer.ActionDescriptor;
import core.dataGateway.DataGateway;
import core.serviceEndPoints.ServiceEndPoint;

import java.util.List;
import java.util.function.Function;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class PipelineBuilder<IncomingType, OutgoingType, ResponseType, ActionType extends ActionDescriptor> {
    private ServiceEndPoint serviceEndPoint;
    private AccessController accessController;
    private List<Function> incomingSerializationSteps;
    private List<Function> outgoingSerializationSteps;
    private DataGateway<OutgoingType, ResponseType, ActionType> dataGateway;

    public PipelineBuilder(){
        this.incomingSerializationSteps = Lists.newArrayList();
        this.outgoingSerializationSteps = Lists.newArrayList();
    }

    public PipelineBuilder setServiceEndPoint(ServiceEndPoint serviceEndPoint){
        this.serviceEndPoint = serviceEndPoint;
        return this;
    }

    public PipelineBuilder setDataGateway(DataGateway dataGateway){
        this.dataGateway = dataGateway;
        return this;
    }

    /**
     *  Theses Functions will be executed in order to transform the object given by the service endpoint to an object
     *  accepted by the datagateway
     * @param step
     * @param <FromType>
     * @param <ToType>
     * @return
     */
    public <FromType, ToType> PipelineBuilder addIncomingSerializationStep(Function<FromType, ToType> step){
        this.incomingSerializationSteps.add(step);
        return this;
    }

    /**
     * Inversely these functions will be executed in order from the result type of the data gateway to transform to an arbitrary result type
     * @param step
     * @param <FromType>
     * @param <ToType>
     * @return
     */
    public <FromType, ToType> PipelineBuilder addOutgoingSerializationStep(Function<FromType, ToType> step){
        this.outgoingSerializationSteps.add(step);
        return this;
    }

    public PipelineBuilder setAccessController(AccessController accessController){
        this.accessController = accessController;
        return this;
    }

    public Pipeline<IncomingType, OutgoingType, ResponseType, ActionType> build(){
        if(serviceEndPoint == null){
            throw new IllegalStateException("Service endpoint not set");
        }
        if(dataGateway == null){
            throw new IllegalStateException("Data Gateway not set");
        }
        Pipeline result =  new Pipeline(serviceEndPoint, accessController, dataGateway, incomingSerializationSteps, outgoingSerializationSteps);
        this.serviceEndPoint.injectPipeline(result);
        return result;
    }


}
