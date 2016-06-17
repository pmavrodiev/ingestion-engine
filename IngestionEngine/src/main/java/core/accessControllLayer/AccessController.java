package core.accessControllLayer;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public interface AccessController {
    boolean authenticateAction(ActionDescriptor action);
}
