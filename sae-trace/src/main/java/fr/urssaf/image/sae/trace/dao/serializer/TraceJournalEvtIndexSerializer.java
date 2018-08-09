/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;

/**
 * Sérialiseur / désérialiseur de {@link TraceJournalEvtIndex}. Utilise un
 * sérailiseur JSON.
 * 
 */
public final class TraceJournalEvtIndexSerializer extends
      JacksonSerializer<TraceJournalEvtIndex> {

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe à sérialiser
    */
   private TraceJournalEvtIndexSerializer(Class<TraceJournalEvtIndex> clazz) {
      super(clazz);
   }

   /**
    * Retourne l'instance de {@link TraceJournalEvtIndexSerializer}
    * 
    * @return le sérialiseur de TraceJournalEvtIndex
    */
   public static TraceJournalEvtIndexSerializer get() {
      return TraceJournalEvtIndexIndexSerializerHolder.INSTANCE;
   }

   /**
    * Classe contenant l'instance de {@link TraceJournalEvtIndexSerializer}
    */
   private static final class TraceJournalEvtIndexIndexSerializerHolder {
      private TraceJournalEvtIndexIndexSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final TraceJournalEvtIndexSerializer INSTANCE = new TraceJournalEvtIndexSerializer(
            TraceJournalEvtIndex.class);
   }

}
