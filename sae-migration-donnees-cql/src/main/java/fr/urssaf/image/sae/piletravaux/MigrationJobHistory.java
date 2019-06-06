/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobHistoryDaoCql;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobHistoryCql;
import fr.urssaf.image.sae.pile.travaux.service.impl.JobQueueServiceImpl;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;

/**
 * Migration de CF JobHistory
 */
@Component
public class MigrationJobHistory {

   private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobHistory.class);

   @Autowired
   IGenericJobTypeDao genericdao;

   @Autowired
   JobHistoryDao daoThrift;

   @Autowired
   JobQueueServiceImpl serviceThrift;

   @Autowired
   IJobHistoryDaoCql cqldao;

   @Autowired
   private CassandraClientFactory ccf;

   // String keyspace = "SAE";

   /**
    * Migration de la CF Thrift vers la CF cql
    */
   public int migrationFromThriftToCql() {

      LOGGER.info(" MigrationJobHistory - migrationFromThriftToCql - start ");

      final Iterator<GenericJobType> it = genericdao.findAllByCFName("JobHistory", ccf.getKeyspace().getKeyspaceName());

      UUID lastKey = null;
      if (it.hasNext()) {
         final Row row = (Row) it.next();
         lastKey = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
      }

      Map<UUID, String> trace = new HashMap<>();
      JobHistoryCql jobH;
      int nb = 0;
      UUID key = null;

      while (it.hasNext()) {

         // Extraction de la clé

         final Row row = (Row) it.next();
         key = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));

         // compare avec la derniere clé qui a été extraite
         // Si different, cela veut dire qu'on passe sur des colonnes avec une nouvelle clé
         // alors on enrgistre celui qui vient d'être traité
         if (key != null && !key.equals(lastKey)) {

            jobH = new JobHistoryCql();
            jobH.setIdjob(lastKey);
            jobH.setTrace(trace);

            // enregistrement
            cqldao.save(jobH);

            // réinitialisation
            lastKey = key;
            trace = new HashMap<>();
            nb++;
         }

         // extraction de la colonne
         final UUID columnName = row.getUUID("column1");

         // extraction de la value
         final String message = StringSerializer.get().fromByteBuffer(row.getBytes("value"));

         trace.put(columnName, message);

      }
      if (key != null) {

         jobH = new JobHistoryCql();
         jobH.setIdjob(key);
         jobH.setTrace(trace);

         // enregistrement
         cqldao.save(jobH);

         nb++;
      }

      LOGGER.debug(" Totale : " + nb);
      LOGGER.debug(" MigrationJobHistory - migrationFromThriftToCql - end");

      return nb;
   }

   /**
    * Migration de la CF cql vers la CF Thrift
    */
   public void migrationFromCqlTothrift() {

      LOGGER.info(" MigrationJobHistory - migrationFromCqlToThrift start ");

      final Iterator<JobHistoryCql> it = cqldao.findAllWithMapper();
      int nb = 0;
      while (it.hasNext()) {
         // final Row row = (Row) it.next();
         final JobHistoryCql jobcql = it.next();
         final UUID idIob = jobcql.getIdjob();

         for (final Entry<UUID, String> entry : jobcql.getTrace().entrySet()) {
            final GenericJobType job = new GenericJobType();
            job.setKey(UUIDSerializer.get().toByteBuffer(idIob));
            job.setColumn1(entry.getKey());
            job.setValue(StringSerializer.get().toByteBuffer(entry.getValue()));
            serviceThrift.addHistory(idIob, entry.getKey(), entry.getValue());
         }
         nb++;
      }

      LOGGER.debug(" Totale : " + nb);
      LOGGER.debug(" MigrationJobHistory - migrationFromCqlToThrift end");
   }

}
