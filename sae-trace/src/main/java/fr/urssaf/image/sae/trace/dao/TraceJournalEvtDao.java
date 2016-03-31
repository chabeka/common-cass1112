/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Service DAO de la famille de colonnes "TraceJournalEvt"
 * 
 */
@Repository
public class TraceJournalEvtDao extends AbstractTraceDao {

   /** contexte de la trace */
   public static final String COL_CONTEXT = "contexte";

   /** nom de la Column Family */
   public static final String REG_EXPLOIT_CFNAME = "TraceJournalEvt";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceJournalEvtDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return REG_EXPLOIT_CFNAME;
   }

   /**
    * ajoute une colonne {@value #COL_CONTEXT}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnContext(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CONTEXT, value, StringSerializer.get(), clock);
   }
}
