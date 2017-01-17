package util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joda.time.DateTime;

public class DateTimeSerializer extends JsonSerializer<DateTime> {
    @Override
    public void serialize(DateTime date, JsonGenerator generator, SerializerProvider provider)
            throws java.io.IOException, JsonProcessingException {

        generator.writeString(date.toString());
    }
}
