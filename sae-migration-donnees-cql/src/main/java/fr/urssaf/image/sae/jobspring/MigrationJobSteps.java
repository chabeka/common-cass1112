/**
 *
 */
package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobStepsCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepsDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

/**
 * Classe permettant de faire la migration de données de la table {@link JobSteps}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobSteps extends MigrationJob implements IMigration {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationJobSteps.class);

  @Autowired
  IJobStepsDaoCql jobStepsExeDao;

  @Override
  public void migrationFromThriftToCql() {

    LOG.info(" MigrationJobSteps - migrationFromThriftToCql - DEBUT ");


    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBSTEPS_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    int nb = 0;
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
      jobStepsExeDao.saveWithMapper(jobcql);
      nb++;
    }

    LOG.info(" MigrationJobSteps - migrationFromThriftToCql - FIN   ");
    LOG.info(" MigrationJobSteps - migrationFromThriftToCql - Total:  {} ", nb);

  }

  @Override
  public void migrationFromCqlTothrift() {

    LOG.info(" MigrationJobSteps - migrationFromCqlTothrift - DEBUT ");

    final Serializer<String> sSlz = StringSerializer.get();
    final Serializer<byte[]> bSlz = BytesArraySerializer.get();
    final Serializer<Long> lSlz = LongSerializer.get();
    final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

    final Iterator<JobStepsCql> it = jobStepsExeDao.findAllWithMapper();
    int nb = 0;
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
      nb++;
    }

    LOG.info(" MigrationJobSteps - migrationFromCqlTothrift - FIN   ");
    LOG.info(" MigrationJobSteps - migrationFromCqlTothrift - Total:  {} ", nb);

  }

  //############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobStepsCql() {

    // liste d'objet cql venant de la base thrift après transformation
    final List<JobStepsCql> listJobThrift = getListJobStepsCqlFromThrift();

    // liste venant de la base cql
    final List<JobStepsCql> listJobCql = new ArrayList<>();
    final Iterator<JobStepsCql> it = jobStepsExeDao.findAllWithMapper();
    while (it.hasNext()) {
      final JobStepsCql jobExToJR = it.next();        
      listJobCql.add(jobExToJR);	    	
    }

    // comparaison de deux listes
    final boolean isBasEq = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isBasEq) {
      LOG.info("MIGRATION_JobStepsCql -- Les listes JobSteps sont identiques");
    } else {
      LOG.warn("MIGRATION_JobStepsCql -- ATTENTION: Les listes JobSteps sont différentes ");
    }

    return isBasEq;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * @return
   */
  public List<JobStepsCql> getListJobStepsCqlFromThrift() {

    final List<JobStepsCql> listJobThrift = new ArrayList<>();

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
      listJobThrift.add(jobcql);
    }     
    return listJobThrift;
  }
}
