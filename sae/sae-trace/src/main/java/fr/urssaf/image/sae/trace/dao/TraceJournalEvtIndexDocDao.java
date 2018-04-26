/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexDoc;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexDocSerializer;

/**
 * Service DAO de la famille de colonnes "TraceJournalEvtIndexDoc"
 * 
 */
@Repository
public class TraceJournalEvtIndexDocDao extends
      AbstractTraceIndexDao<TraceJournalEvtIndexDoc> {
   
   /**
    * Constantes pour le nom de la colonne familly de l’index par l’id du document
    */
   public static final String JOURNAL_EVT_INDEX_DOC_CFNAME = "TraceJournalEvtIndexDoc";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisés
    */
   @Autowired
   public TraceJournalEvtIndexDocDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Méthode de suppression d'une ligne TraceJournalEvtIndexDoc
    * 
    * @param mutator
    *           Mutator de {@link TraceJournalEvtIndexDoc}
    * @param code
    *           identifiant de la ligne d'index
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceJournalEvtIndexDoc(
         Mutator<String> mutator, String code, long clock) {

      mutatorSuppressionLigne(mutator, code, clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getColumnFamilyName() {
      return JOURNAL_EVT_INDEX_DOC_CFNAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Serializer<TraceJournalEvtIndexDoc> getValueSerializer() {
      return TraceJournalEvtIndexDocSerializer.get();
   }
   
   /**
    * 
    * Permet de supprimer l’index d’un document
    * 
    * @param mutator Mutator
    * @param idDoc Identifiant du document
    * @param clock Horloge de suppression
    */
   public void deleteIndex(Mutator<String> mutator, String idDoc, long clock){
      mutatorSuppressionLigne(mutator, idDoc, clock);
   }
}
