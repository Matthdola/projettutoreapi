package util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.joda.time.DateTime;

import java.io.IOException;


public class DateTimeDeserializer extends JsonDeserializer<DateTime> {
    @Override
    public DateTime deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException {

        String iso8601date = parser.getValueAsString();

        return DateTime.parse(iso8601date);
    }
}
