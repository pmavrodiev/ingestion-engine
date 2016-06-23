package impls.KafkaREST;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author Graf_Blutwurst
 * @Date 6/23/2016
 */
public class KafkaRESTAppender extends AppenderSkeleton {
    private boolean hasErrors;
    private List<LoggingEvent> events;


    private static final class LogLine{
        private final String level;
        private final String message;

        public LogLine(String level, String message) {
            this.level = level;
            this.message = message;
        }

        public String getLevel() {
            return level;
        }

        public String getMessage() {
            return message;
        }
    }

    public KafkaRESTAppender(){
        this.hasErrors = false;
        this.events = Lists.newArrayList();
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if(loggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)){
            this.hasErrors = true;
        }
        this.events.add(loggingEvent);
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    public boolean isHasErrors(){
        return hasErrors;
    }

    public void reset(){
        this.events = Lists.newArrayList();
    }

    public List<JsonObject> getLogLines(){
        final Gson gson = new Gson();
        final JsonParser parser = new JsonParser();
        return events.stream()
                .map(
                        loggingEvent -> {
                            return parser.parse(
                                    gson.toJson(
                                            new LogLine(
                                                    loggingEvent.getLevel().toString(),
                                                    loggingEvent.getMessage().toString()
                                            )
                                    )
                            ).getAsJsonObject();
                        }
                ).collect(Collectors.toList());
    }
}
