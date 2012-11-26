/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;

/**
 * Sérialiseur / désérialiseur de {@link TraceRegSecuriteIndex}. Utilise un
 * sérailiseur JSON.
 * 
 */
public class TraceRegSecuriteIndexSerializer extends
      JacksonSerializer<TraceRegSecuriteIndex> {

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe à sérialiser
    */
   private TraceRegSecuriteIndexSerializer(Class<TraceRegSecuriteIndex> clazz) {
      super(clazz);
   }

   /**
    * Retourne l'instance de {@link TraceRegSecuriteIndexSerializer}
    * 
    * @return
    */
   public static TraceRegSecuriteIndexSerializer getInstance() {
      return TraceRegSecuriteIndexSerializerHolder.INSTANCE;
   }

   /**
    * Classe contenant l'instance de {@link TraceRegSecuriteIndexSerializer}
    */
   private static class TraceRegSecuriteIndexSerializerHolder {
      private TraceRegSecuriteIndexSerializerHolder() {
      }

      private static final TraceRegSecuriteIndexSerializer INSTANCE = new TraceRegSecuriteIndexSerializer(
            TraceRegSecuriteIndex.class);
   }

}
