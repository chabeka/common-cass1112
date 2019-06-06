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

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceToJobExecutionDaoCql;
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
public class MigrationJobinstanceToJobExecution extends MigrationJob implements IMigration {

   private static final Logger LOG = LoggerFactory.getLogger(MigrationJobinstanceToJobExecution.class);

   @Autowired
   IJobInstanceToJobExecutionDaoCql jobInstToJobEx;

   @Override
   public void migrationFromThriftToCql() {
      if (LOG.isDebugEnabled()) {
         LOG.debug(" _____________________________________________________");
         LOG.debug("                                                      ");
         LOG.debug(" MigrationJobinstanceToJobExecution - migrationFromThriftToCql - DEBUT ");
         LOG.debug(" _____________________________________________________");
      }

      final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

      while (it.hasNext()) {
         final Row row = (Row) it.next();
         final Long idInst = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
         final Long idJobEx = row.getLong("column1");
         final JobInstanceToJobExecutionCql jobInstToJ = new JobInstanceToJobExecutionCql();
         jobInstToJ.setJobInstanceId(idInst);
         jobInstToJ.setJobExecutionId(idJobEx);
         jobInstToJ.setValue("");
         jobInstToJobEx.save(jobInstToJ);
      }
      if (LOG.isDebugEnabled()) {
         LOG.debug(" _____________________________________________________");
         LOG.debug("                                                      ");
         LOG.debug(" MigrationJobinstanceToJobExecution - migrationFromThriftToCql - FIN   ");
         LOG.debug(" _____________________________________________________");
      }
   }

   @Override
   public void migrationFromCqlTothrift() {

      if (LOG.isDebugEnabled()) {
         LOG.debug(" _____________________________________________________");
         LOG.debug("                                                      ");
         LOG.debug(" MigrationJobinstanceToJobExecution - migrationFromCqlTothrift - DEBUT ");
         LOG.debug(" _____________________________________________________");
      }

      final Serializer<String> sSlz = StringSerializer.get();
      final Serializer<byte[]> bSlz = BytesArraySerializer.get();
      final Serializer<Long> lSlz = LongSerializer.get();
      final byte[] empty = new byte[0];
      ;
      final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

      final Iterator<JobInstanceToJobExecutionCql> it = jobInstToJobEx.findAllWithMapper();

      while (it.hasNext()) {
         final JobInstanceToJobExecutionCql jobInstToJ = it.next();
         final Long instId = jobInstToJ.getJobInstanceId();
         final Long exeId = jobInstToJ.getJobExecutionId();

         mutator.addInsertion(lSlz.toBytes(instId),
                              Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME,
                              HFactory.createColumn(exeId, empty, lSlz, bSlz));
         mutator.execute();
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug(" _____________________________________________________");
         LOG.debug("                                                      ");
         LOG.debug(" MigrationJobinstanceToJobExecution - migrationFromCqlTothrift - FIN   ");
         LOG.debug(" _____________________________________________________");
      }

   }

}
