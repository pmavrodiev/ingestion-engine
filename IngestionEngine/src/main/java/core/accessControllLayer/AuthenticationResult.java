package core.accessControllLayer;

/**
 * @author Graf_Blutwurst
 * @Date 6/23/2016
 */
public class AuthenticationResult {
    private final Boolean success;
    private final String authenticationMessage;


    public AuthenticationResult(Boolean success, String authenticationMessage) {
        this.authenticationMessage = authenticationMessage;
        this.success = success;
    }

    public AuthenticationResult(Boolean success) {
        this(success, "");
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getAuthenticationMessage() {
        return authenticationMessage;
    }
}
