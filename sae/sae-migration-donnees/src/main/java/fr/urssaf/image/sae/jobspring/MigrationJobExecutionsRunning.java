/**
 *
 */
package fr.urssaf.image.sae.jobspring;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionsRunningCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionsRunningDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 *
 *
 */
@Component
public class MigrationJobExecutionsRunning extends MigrationJob implements IMigration {

   private static final Logger LOG = LoggerFactory.getLogger(MigrationJobinstanceToJobExecution.class);

   @Autowired
   IJobExecutionsRunningDaoCql jobExeToJobR;

   @Override
   public void migrationFromThriftToCql() {
      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobExecutionsRunning - migrationFromThriftToCql - DEBUT ");
      }

      final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBEXECUTIONS_RUNNING_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

      while (it.hasNext()) {
         final Row row = (Row) it.next();
         final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
         if (!Constante.ALL_JOBS_KEY.equals(key)) {
            final Long idJobEx = row.getLong("column1");
            final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
            final JobExecutionsRunningCql jobExes = new JobExecutionsRunningCql();
            jobExes.setJobExecutionId(idJobEx);
            jobExes.setJobName(key);
            jobExes.setValue(value);

            jobExeToJobR.save(jobExes);
         }

      }
      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobExecutionsRunning - migrationFromThriftToCql - FIN   ");
      }
   }

   @Override
   public void migrationFromCqlTothrift() {

      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobExecutionsRunning - migrationFromCqlTothrift - DEBUT ");
      }

      final Serializer<String> sSlz = StringSerializer.get();
      final Serializer<byte[]> bSlz = BytesArraySerializer.get();
      final Serializer<Long> lSlz = LongSerializer.get();
      final byte[] empty = new byte[0];

      final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

      final Iterator<JobExecutionsRunningCql> it = jobExeToJobR.findAllWithMapper();

      while (it.hasNext()) {
         final JobExecutionsRunningCql jobExToJR = it.next();
         final String jobName = jobExToJR.getJobName();
         final Long jobExecutionId = jobExToJR.getJobExecutionId();

         mutator.addInsertion(sSlz.toBytes(jobName),
                              Constante.JOBEXECUTIONS_RUNNING_CFNAME,
                              HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
         mutator.addInsertion(sSlz.toBytes(Constante.ALL_JOBS_KEY),
                              Constante.JOBEXECUTIONS_RUNNING_CFNAME,
                              HFactory.createColumn(jobExecutionId, empty, lSlz, bSlz));
         mutator.execute();
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobExecutionsRunning - migrationFromCqlTothrift - FIN   ");
      }

   }

}
