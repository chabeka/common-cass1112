/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexSerializer;

/**
 * Service DAO de la famille de colonnes "TraceJournalEvtIndex"
 * 
 */
@Repository
public class TraceJournalEvtIndexDao {

   private static final int MAX_ATTRIBUTS = 100;
   public static final String JOURNAL_EVT_INDEX_CFNAME = "TraceJournalEvtIndex";

   private final ColumnFamilyTemplate<String, UUID> evtIndexTmpl;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisés
    */
   @Autowired
   public TraceJournalEvtIndexDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      evtIndexTmpl = new ThriftColumnFamilyTemplate<String, UUID>(keyspace,
            JOURNAL_EVT_INDEX_CFNAME, StringSerializer.get(), UUIDSerializer
                  .get());

      evtIndexTmpl.setCount(MAX_ATTRIBUTS);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<String, UUID> updater,
         UUID colName, Object value, Serializer valueSerializer, long clock) {

      HColumn<UUID, Object> column = HFactory.createColumn(colName, value,
            UUIDSerializer.get(), valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne <b>name</b>
    * 
    * @param updater
    *           updater de <b>TraceJournalEvtIndex</b>
    * @param name
    *           nom de la colonne
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumn(ColumnFamilyUpdater<String, UUID> updater,
         UUID name, TraceJournalEvtIndex value, long clock) {
      addColumn(updater, name, value, TraceJournalEvtIndexSerializer.get(),
            clock);
   }

   /**
    * 
    * @return SliceQuery de <code>TraceJournalEvtIndex</code>
    */
   public final SliceQuery<String, UUID, TraceJournalEvtIndex> createSliceQuery() {

      SliceQuery<String, UUID, TraceJournalEvtIndex> sliceQuery = HFactory
            .createSliceQuery(keyspace, StringSerializer.get(), UUIDSerializer
                  .get(), TraceJournalEvtIndexSerializer.get());

      sliceQuery.setColumnFamily(JOURNAL_EVT_INDEX_CFNAME);

      return sliceQuery;
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

      mutator.addDeletion(code, JOURNAL_EVT_INDEX_CFNAME, clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceJournalEvtIndex</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   /**
    * Création du ColumnFamilyUpdater
    * 
    * @param journee
    *           la journée, au format obtenu de {@link #getJournee(Date)}
    * @return le ColumnFamilyUpdater
    */
   public final ColumnFamilyUpdater<String, UUID> createUpdater(String journee) {
      return evtIndexTmpl.createUpdater(journee);
   }

   /**
    * Flush les mises à jour
    * 
    * @param updater
    *           l'updater de la CF
    */
   public final void update(ColumnFamilyUpdater<String, UUID> updater) {
      evtIndexTmpl.update(updater);
   }
}
