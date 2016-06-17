package core.accessControllLayer;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class DummyAccessController implements AccessController {
    @Override
    public boolean authenticateAction(ActionDescriptor action) {
        return true;
    }
}
