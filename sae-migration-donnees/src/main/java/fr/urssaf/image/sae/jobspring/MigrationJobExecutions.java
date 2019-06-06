package fr.urssaf.image.sae.jobspring;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsDaoCql;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

@Component
public class MigrationJobExecutions extends MigrationJob implements IMigration {

   private static final Logger LOG = LoggerFactory.getLogger(MigrationJobExecutions.class);

   protected static final String ALL_JOBS_KEY = "_all";

   @Autowired
   IJobExecutionsDaoCql jobExesdao;

   protected static final String JOBEXECUTIONS_CFNAME = "JobExecutions";

   @Override
   public void migrationFromThriftToCql() {

      if (LOG.isDebugEnabled()) {
         LOG.debug("MigrationJobExecutions - migrationFromThriftToCql - DEBUT");
      }
      final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(JOBEXECUTIONS_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

      while (it.hasNext()) {
         final Row row = (Row) it.next();
         final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
         final Long id = row.getLong("column1");
         final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
         final JobExecutionsCql jobExes = new JobExecutionsCql();
         jobExes.setJobExecutionId(id);
         jobExes.setJobName(key);
         jobExes.setValue(value);

         // La clé de type String correspond soit au nom du job, soit au mot clé « _all »
         // qui permet de parcourir les exécutions tout job confondu
         // Ce systeme avec le mot clé « _all » ne sert pas dans le cql
         // Donc on enregistre pas les données se trouvant dans la clé « _all »
         if (!ALL_JOBS_KEY.equals(key)) {
            jobExesdao.save(jobExes);
         }
      }
      if (LOG.isDebugEnabled()) {
         LOG.debug("MigrationJobExecutions - migrationFromThriftToCql - FIN");
      }
   }

   @Override
   public void migrationFromCqlTothrift() {
      if (LOG.isDebugEnabled()) {
         LOG.debug("MigrationJobExecutions - migrationFromCqlTothrift - DEBU");
      }
      final Iterator<JobExecutionsCql> it = jobExesdao.findAllWithMapper();

      while (it.hasNext()) {

         final JobExecutionsCql job = it.next();
         final Serializer<String> sSlz = StringSerializer.get();
         final Serializer<Long> lSlz = LongSerializer.get();
         final Serializer<byte[]> bSlz = BytesArraySerializer.get();
         final Long jobExecutionId = job.getJobExecutionId();

         final byte[] empty = new byte[0];
         final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

         // La clé de type String correspond soit au nom du job, soit au mot clé « _all » qui permet de parcourir les exécutions tout job confondu
         mutator.addInsertion(sSlz.toBytes(job.getJobName()), JOBEXECUTIONS_CFNAME, HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
         mutator.addInsertion(sSlz.toBytes(ALL_JOBS_KEY), JOBEXECUTIONS_CFNAME, HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));

         mutator.execute();

      }
      if (LOG.isDebugEnabled()) {
         LOG.debug("MigrationJobExecutions - migrationFromCqlTothrift - FIN");
      }
   }

}
