/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;

/**
 * Sérialiseur / désérialiseur de {@link TraceRegTechniqueIndex}. Utilise un
 * sérailiseur JSON.
 * 
 */
public final class TraceRegTechniqueIndexSerializer extends
      JacksonSerializer<TraceRegTechniqueIndex> {

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe à sérialiser
    */
   private TraceRegTechniqueIndexSerializer(Class<TraceRegTechniqueIndex> clazz) {
      super(clazz);
   }

   /**
    * Retourne l'instance de {@link TraceRegTechniqueIndexSerializer}
    * 
    * @return le sérialiseur de TraceRegTechniqueIndex
    */
   public static TraceRegTechniqueIndexSerializer get() {
      return TraceRegTechniqueIndexSerializerHolder.INSTANCE;
   }

   /**
    * Classe contenant l'instance de {@link TraceRegTechniqueIndexSerializer}
    */
   private static final class TraceRegTechniqueIndexSerializerHolder {
      private TraceRegTechniqueIndexSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final TraceRegTechniqueIndexSerializer INSTANCE = new TraceRegTechniqueIndexSerializer(
            TraceRegTechniqueIndex.class);
   }

}
