package impls.KafkaREST;

import core.accessControllLayer.ActionDescriptor;

/**
 * @author Graf_Blutwurst
 * @Date 6/17/2016
 */
public class KafkaRESTAction extends ActionDescriptor {

    public enum Verb {
        GET,
        POST,
        UPDATE,
        DELETE
    }

    private Verb verb;


    public KafkaRESTAction(Verb verb) {
        this.verb = verb;
    }

    public Verb getVerb() {
        return verb;
    }

    public void setVerb(Verb verb) {
        this.verb = verb;
    }
}
