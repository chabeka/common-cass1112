package fr.urssaf.image.sae.pile.travaux.dao;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * DAO de la colonne famille <code>JobHistory</code>
 * 
 * 
 */
@Repository
public class JobHistoryDao {

   private static final String JOBHISTORY_CFNAME = "JobHistory";

   private static final int MAX_JOB_ATTIBUTS = 100;

   private static final int TTL = 2592000; // 2592000 secondes, soit 30 jours

   private final ColumnFamilyTemplate<UUID, String> jobHistoryTmpl;

   /**
    * 
    * @param keyspace
    *           Keyspace cassandra à utiliser
    */
   @Autowired
   public JobHistoryDao(Keyspace keyspace) {

      // Propriété de clé:
      // - Type de la valeur : UUID
      // - Serializer de la valeur : UUIDSerializer

      jobHistoryTmpl = new ThriftColumnFamilyTemplate<UUID, String>(keyspace,
            JOBHISTORY_CFNAME, UUIDSerializer.get(), StringSerializer.get());

      jobHistoryTmpl.setCount(MAX_JOB_ATTIBUTS);

   }

   public final ColumnFamilyTemplate<UUID, String> getJobHistoryTmpl() {

      return this.jobHistoryTmpl;
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<UUID, String> updater,
         Object colName, Object value, Serializer nameSerializer,
         Serializer valueSerializer, long clock) {

      HColumn<String, Object> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setTtl(TTL);
      column.setClock(clock);
      updater.setColumn(column);

   }

   public final void ecritColonneTrace(
         ColumnFamilyUpdater<UUID, String> updater, UUID timestampTrace,
         String messageTrace, long clock) {

      addColumn(updater, timestampTrace, messageTrace, UUIDSerializer.get(),
            StringSerializer.get(), clock);

   }

}
