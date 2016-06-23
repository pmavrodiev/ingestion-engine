package core.accessControllLayer;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class DummyAccessController implements AccessController {

    private final Boolean alwaysReturn;

    private final static String AUTH_MSG = "Authentication Failed";

    public DummyAccessController() {
        this(true);
    }

    public DummyAccessController(Boolean alwaysReturn){
        this.alwaysReturn = alwaysReturn;
    }

    @Override
    public AuthenticationResult authenticateAction(ActionDescriptor action) {
        return new AuthenticationResult(this.alwaysReturn, AUTH_MSG);
    }
}
