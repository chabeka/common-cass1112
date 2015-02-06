/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexSerializer;

/**
 * Service DAO de la famille de colonnes "TraceJournalEvtIndex"
 * 
 */
@Repository
public class TraceJournalEvtIndexDao extends
      AbstractTraceIndexDao<TraceJournalEvtIndex> {

   public static final String JOURNAL_EVT_INDEX_CFNAME = "TraceJournalEvtIndex";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisés
    */
   @Autowired
   public TraceJournalEvtIndexDao(Keyspace keyspace) {

      super(keyspace);
   }

   /**
    * Méthode de suppression d'une ligne TraceJournalEvtIndex
    * 
    * @param mutator
    *           Mutator de <code>TraceJournalEvtIndex</code>
    * @param code
    *           identifiant de la ligne d'index
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceJournalEvtIndex(
         Mutator<String> mutator, String code, long clock) {

      mutatorSuppressionLigne(mutator, code, clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getColumnFamilyName() {
      return JOURNAL_EVT_INDEX_CFNAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Serializer<TraceJournalEvtIndex> getValueSerializer() {
      return TraceJournalEvtIndexSerializer.get();
   }
}
