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

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 *
 *
 */
@Component
public class MigrationJobSteps extends MigrationJob implements IMigration {

   private static final Logger LOG = LoggerFactory.getLogger(MigrationJobSteps.class);

   @Autowired
   IJobStepsDaoCql jobStepsExeDao;

   @Override
   public void migrationFromThriftToCql() {
      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobSteps - migrationFromThriftToCql - DEBUT ");
      }

      final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBSTEPS_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

      while (it.hasNext()) {
         final Row row = (Row) it.next();
         final Long id = row.getLong("column1");
         final Composite composite = CompositeSerializer.get().fromByteBuffer(row.getBytes("value"));
         final String jobName = (String) composite.getComponent(0).getValue(StringSerializer.get());
         final String stepName = (String) composite.getComponent(1).getValue(StringSerializer.get());
         final JobStepsCql jobcql = new JobStepsCql();
         jobcql.setJobStepId(id);
         jobcql.setJobName(jobName);
         jobcql.setStepName(stepName);
         jobStepsExeDao.save(jobcql);
      }
      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobSteps - migrationFromThriftToCql - FIN   ");
      }
   }

   @Override
   public void migrationFromCqlTothrift() {

      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobSteps - migrationFromCqlTothrift - DEBUT ");
      }

      final Serializer<String> sSlz = StringSerializer.get();
      final Serializer<byte[]> bSlz = BytesArraySerializer.get();
      final Serializer<Long> lSlz = LongSerializer.get();
      final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

      final Iterator<JobStepsCql> it = jobStepsExeDao.findAllWithMapper();

      while (it.hasNext()) {
         final JobStepsCql stepCql = it.next();

         final Long stepId = stepCql.getJobStepId();
         final Composite value = new Composite();
         final String jobName = stepCql.getJobName();
         final String stepName = stepCql.getStepName();

         value.addComponent(jobName, sSlz);
         value.addComponent(stepName, sSlz);
         mutator.addInsertion(sSlz.toBytes(Constante.JOB_STEPS_KEY),
                              Constante.JOBSTEPS_CFNAME,
                              HFactory.createColumn(stepId, value, lSlz, CompositeSerializer.get()));
         mutator.execute();
      }

      if (LOG.isDebugEnabled()) {
         LOG.debug(" MigrationJobSteps - migrationFromCqlTothrift - FIN   ");
      }

   }

}
