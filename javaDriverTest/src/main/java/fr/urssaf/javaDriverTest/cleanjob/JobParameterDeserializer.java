/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest.cleanjob;

import java.io.IOException;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.springframework.batch.core.JobParameter;

/**
 * TODO (ac75007394) Description du type
 */

public class JobParameterDeserializer extends JsonDeserializer<JobParameter> {
   @Override
   public JobParameter deserialize(final JsonParser jp, final DeserializationContext ctxt)
         throws IOException, JsonProcessingException {
      if (jp.getCurrentToken() == null) {
         jp.nextToken();
      }
      if (jp.getCurrentToken() != JsonToken.START_OBJECT) {
         throw new IOException("Expected data to start with an object");
      }
      JobParameter.ParameterType type = null;
      String value = null;
      while (jp.nextToken() != JsonToken.END_OBJECT) {
         final String fieldName = jp.getCurrentName();
         jp.nextToken();
         if (fieldName.equals("parameterType")) {
            type = JobParameter.ParameterType.valueOf(jp.getText());
         } else if (fieldName.equals("parameter")) {
            value = jp.getText();
         } else {
            throw new IOException("Unrecognized field : " + fieldName);
         }
      }
      switch (type) {
      case STRING:
         return new JobParameter(value);
      case DATE:
         return new JobParameter(new Date(Long.valueOf(value).longValue()));
      case DOUBLE:
         return new JobParameter(Double.valueOf(value));
      case LONG:
         return new JobParameter(Long.valueOf(value));
      }
      throw new IOException("Couldn't parse JobParameter JSON, value found = " + value + ", type found = " + type);
   }
}