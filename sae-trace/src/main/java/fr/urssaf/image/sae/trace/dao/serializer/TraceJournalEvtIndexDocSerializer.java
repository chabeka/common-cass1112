/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;

/**
 * Sérialiseur / désérialiseur de {@link TraceJournalEvtIndexDoc}. Utilise un
 * sérailiseur JSON.
 * 
 */
public final class TraceJournalEvtIndexDocSerializer extends
      JacksonSerializer<TraceJournalEvtIndexDoc> {

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe à sérialiser
    */
   private TraceJournalEvtIndexDocSerializer(Class<TraceJournalEvtIndexDoc> clazz) {
      super(clazz);
   }

   /**
    * Retourne l'instance de {@link TraceJournalEvtIndexDocSerializer}
    * 
    * @return le sérialiseur de {@link TraceJournalEvtIndexDoc}
    */
   public static TraceJournalEvtIndexDocSerializer get() {
      return TraceJournalEvtIndexIndexDocSerializerHolder.INSTANCE;
   }

   /**
    * Classe contenant l'instance de {@link TraceJournalEvtIndexDocSerializer}
    */
   private static final class TraceJournalEvtIndexIndexDocSerializerHolder {
      private TraceJournalEvtIndexIndexDocSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final TraceJournalEvtIndexDocSerializer INSTANCE = new TraceJournalEvtIndexDocSerializer(
            TraceJournalEvtIndexDoc.class);
   }

}
