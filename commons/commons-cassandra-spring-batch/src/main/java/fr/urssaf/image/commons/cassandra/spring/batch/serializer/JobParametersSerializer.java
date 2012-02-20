package fr.urssaf.image.commons.cassandra.spring.batch.serializer;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;

import fr.urssaf.image.commons.cassandra.serializer.ObjectToJsonSerializer;
import fr.urssaf.image.commons.cassandra.serializer.exception.SerializerException;

import me.prettyprint.cassandra.serializers.AbstractSerializer;

/**
 * Classe de sérialisation/désérialisation des JobParameters
 * Elle utilise un sérialiser json.
 */
public class JobParametersSerializer extends AbstractSerializer<JobParameters> {

   private static final JobParametersSerializer INSTANCE = new JobParametersSerializer();

   @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops", "unchecked", "PMD.IfElseStmtsMustUseBraces" })   
   @Override
   public JobParameters fromByteBuffer(ByteBuffer byteBuffer) {
      Map<String, Object> mapObject = (Map<String, Object>) ObjectToJsonSerializer
            .get().fromByteBuffer(byteBuffer);
      Map<String, JobParameter> mapParam = new HashMap<String, JobParameter>(
            mapObject.size());
      for (String key : mapObject.keySet()) {
         Object value = mapObject.get(key);
         if (value instanceof Date)
            mapParam.put(key, new JobParameter((Date) value));
         else if (value instanceof Long)
            mapParam.put(key, new JobParameter((Long) value));
         else if (value instanceof String)
            mapParam.put(key, new JobParameter((String) value));
         else if (value instanceof Double)
            mapParam.put(key, new JobParameter((Double) value));
         else
            throw new SerializerException(
                  "Erreur lors de la désérialisation : la classe de la valeur ("
                        + value.getClass() + ") n'est pas prévue");
      }
      return new JobParameters(mapParam);
   }

   @Override
   public ByteBuffer toByteBuffer(JobParameters jobParameters) {
      Map<String, JobParameter> mapParam = jobParameters.getParameters();
      // On transforma la map de JobParameter en map d'objets
      Map<String, Object> mapObject = new HashMap<String, Object>(mapParam
            .size());
      for (String key : mapParam.keySet()) {
         JobParameter parameter = mapParam.get(key);
         mapObject.put(key, parameter.getValue());
      }
      return ObjectToJsonSerializer.get().toByteBuffer(mapObject);
   }

   /**
    * Renvoie un singleton
    * @return  singleton
    */
   public static JobParametersSerializer get() {
      return INSTANCE;
   }

}
