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

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionToJobStepDaoCql;
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
public class MigrationJobExecutionToJobStep extends MigrationJob implements IMigration {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationJobExecutionToJobStep.class);

  @Autowired
  IJobExecutionToJobStepDaoCql jobExeToJobStepDao;

  @Override
  public void migrationFromThriftToCql() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigationJobExecutionToJobStep - migrationFromThriftToCql - DEBUT");
    }

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBEXECUTION_TO_JOBSTEP_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final Long jobExecutionId = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      final Long idStep = row.getLong("column1");
      final byte[] value = BytesArraySerializer.get().fromByteBuffer(row.getBytes("value"));

      final JobExecutionToJobStepCql jobcql = new JobExecutionToJobStepCql();
      jobcql.setJobStepId(idStep);
      jobcql.setJobExecutionId(jobExecutionId);
      // la value n'est pas renseignée dans l'ancien système
      jobcql.setValue("");
      jobExeToJobStepDao.save(jobcql);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigationJobExecutionToJobStep - migrationFromThriftToCql - FIN");
    }
  }

  @Override
  public void migrationFromCqlTothrift() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigationJobExecutionToJobStep - migrationFromCqlTothrift - DEBUT");
    }

    final Iterator<JobExecutionToJobStepCql> it = jobExeToJobStepDao.findAllWithMapper();
    final Serializer<String> sSlz = StringSerializer.get();
    final Serializer<byte[]> bSlz = BytesArraySerializer.get();
    final Serializer<Long> lSlz = LongSerializer.get();
    final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

    while (it.hasNext()) {
      final JobExecutionToJobStepCql jobStepExeCql = it.next();

      // Dans JobExecutionToJobStep
      // clé = jobExecutionId
      // Nom de colonne = jobStepId
      // Valeur = vide
      mutator.addInsertion(lSlz.toBytes(jobStepExeCql.getJobExecutionId()),
                           Constante.JOBEXECUTION_TO_JOBSTEP_CFNAME,
                           HFactory.createColumn(jobStepExeCql.getJobStepId(), new byte[0], lSlz, bSlz));
      mutator.execute();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("MigationJobExecutionToJobStep - migrationFromCqlTothrift - FIN");
    }

  }

}
