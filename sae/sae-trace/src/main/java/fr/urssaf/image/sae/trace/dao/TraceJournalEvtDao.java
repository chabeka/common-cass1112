/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.DateSerializer;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

/**
 * Service DAO de la famille de colonnes "TraceJournalEvt"
 * 
 */
@Repository
public class TraceJournalEvtDao {

   /** Date de création de la trace */
   public static final String COL_TIMESTAMP = "timestamp";

   /** contexte de la trace */
   public static final String COL_CONTEXT = "contexte";

   /** Code de l'événement */
   public static final String COL_CODE_EVT = "codeEvt";

   /** Code du contrat de service */
   public static final String COL_CONTRAT_SERVICE = "cs";

   /** Le ou les PAGMS */
   public static final String COL_PAGMS = "pagms";

   /** Identifiant utilisateur */
   public static final String COL_LOGIN = "login";

   /** Informations complémentaires */
   public static final String COL_INFOS = "infos";

   private static final int MAX_ATTRIBUTS = 100;
   public static final String REG_EXPLOIT_CFNAME = "TraceJournalEvt";

   private final ColumnFamilyTemplate<UUID, String> journalEvtTmpl;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceJournalEvtDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      journalEvtTmpl = new ThriftColumnFamilyTemplate<UUID, String>(keyspace,
            REG_EXPLOIT_CFNAME, UUIDSerializer.get(), StringSerializer.get());

      journalEvtTmpl.setCount(MAX_ATTRIBUTS);
   }

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_TIMESTAMP}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnTimestamp(
         ColumnFamilyUpdater<UUID, String> updater, Date value, long clock) {
      addColumn(updater, COL_TIMESTAMP, value, DateSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_CONTEXT}
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

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_CODE_EVT}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnCodeEvt(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CODE_EVT, value, StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_CONTRAT_SERVICE}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnContratService(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CONTRAT_SERVICE, value, StringSerializer.get(),
            clock);
   }

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_PAGMS}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnPagms(
         ColumnFamilyUpdater<UUID, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_PAGMS, value, ListSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_LOGIN}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnLogin(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_LOGIN, value, StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceJournalEvtDao#COL_INFOS}
    * 
    * @param updater
    *           updater de <b>TraceJournalEvt</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnInfos(
         ColumnFamilyUpdater<UUID, String> updater, Map<String, Object> value,
         long clock) {
      addColumn(updater, COL_INFOS, value, MapSerializer.get(), clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceJournalEvt</code>
    */
   public final Mutator<UUID> createMutator() {

      Mutator<UUID> mutator = HFactory.createMutator(keyspace, UUIDSerializer
            .get());

      return mutator;

   }

   /**
    * Méthode de suppression d'une ligne TraceJournalEvt
    * 
    * @param mutator
    *           Mutator de <code>TraceJournalEvt</code>
    * @param code
    *           identifiant de la trace
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionRegExploitation(Mutator<UUID> mutator,
         UUID code, long clock) {

      mutator.addDeletion(code, REG_EXPLOIT_CFNAME, clock);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<UUID, String> updater,
         String colName, Object value, Serializer valueSerializer, long clock) {

      HColumn<String, Object> column = HFactory.createColumn(colName, value,
            StringSerializer.get(), valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);
   }

   /**
    * @return le CassandraTemplate de <code>TraceJournalEvt</code>
    */
   public final ColumnFamilyTemplate<UUID, String> getJournalEvtTmpl() {
      return journalEvtTmpl;
   }

}
