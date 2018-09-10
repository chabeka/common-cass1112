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
public final class TraceRegSecuriteIndexSerializer extends
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
    * @return le sérialiseur de TraceRegSecuriteIndex
    */
   public static TraceRegSecuriteIndexSerializer get() {
      return TraceRegSecuriteIndexSerializerHolder.INSTANCE;
   }

   /**
    * Classe contenant l'instance de {@link TraceRegSecuriteIndexSerializer}
    */
   private static final class TraceRegSecuriteIndexSerializerHolder {
      private TraceRegSecuriteIndexSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final TraceRegSecuriteIndexSerializer INSTANCE = new TraceRegSecuriteIndexSerializer(
            TraceRegSecuriteIndex.class);
   }

}
