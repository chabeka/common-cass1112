/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
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

import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

/**
 * Service DAO de la famille de colonnes "TraceRegTechnique"
 * 
 */
@Repository
public class TraceRegTechniqueDao {

   /** Date de création de la trace */
   public static final String COL_TIMESTAMP = "timestamp";

   /** Contexte de l'événement */
   public static final String COL_CONTEXTE = "contexte";

   /** code de l'événement */
   public static final String COL_CODE_EVT = "codeEvt";

   /** code du contrat de service */
   public static final String COL_CONTRAT_SERVICE = "cs";

   /** identifiant utilisateur */
   public static final String COL_LOGIN = "login";
   
   /** informations supplémentaires */
   public static final String COL_INFOS = "infos";

   /** trace d'erreur */
   public static final String COL_STACKTRACE = "stacktrace";

   private static final int MAX_ATTRIBUTS = 100;
   public static final String REG_TECHNIQUE_CFNAME = "TraceRegTechnique";

   private final ColumnFamilyTemplate<UUID, String> techTmpl;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public TraceRegTechniqueDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      techTmpl = new ThriftColumnFamilyTemplate<UUID, String>(keyspace,
            REG_TECHNIQUE_CFNAME, UUIDSerializer.get(), StringSerializer.get());

      techTmpl.setCount(MAX_ATTRIBUTS);
   }

   /**
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_TIMESTAMP}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
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
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_CONTEXTE}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnContexte(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CONTEXTE, value, StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_CODE_EVT}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
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
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_CONTRAT_SERVICE}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
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
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_LOGIN}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
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
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_STACKTRACE}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnStackTrace(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_STACKTRACE, value, StringSerializer.get(), clock);
   }
   
   /**
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_INFOS}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnInfos(
         ColumnFamilyUpdater<UUID, String> updater, Map<String, String> value,
         long clock) {
      addColumn(updater, COL_INFOS, value, MapSerializer.get(), clock);
   }

   /**
    * Méthode de suppression d'une ligne TraceRegTechnique
    * 
    * @param mutator
    *           Mutator de <code>TraceRegTechnique</code>
    * @param code
    *           identifiant de la trace
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceRegTechnique(Mutator<UUID> mutator,
         UUID code, long clock) {

      mutator.addDeletion(code, REG_TECHNIQUE_CFNAME, clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceRegTechnique</code>
    */
   public final Mutator<UUID> createMutator() {

      Mutator<UUID> mutator = HFactory.createMutator(keyspace, UUIDSerializer
            .get());

      return mutator;

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
    * @return le CassandraTemplate de <code>TraceRegTechnique</code>
    */
   public final ColumnFamilyTemplate<UUID, String> getTechTmpl() {
      return techTmpl;
   }
}
