package core.accessControllLayer;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public interface AccessController<ActionType, IncomingType> {
    AuthenticationResult authenticateAction(ActionType action, IncomingType content);
}
