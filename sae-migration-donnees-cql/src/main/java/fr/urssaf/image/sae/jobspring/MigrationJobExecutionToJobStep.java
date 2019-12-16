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

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobExecutionToJobStepCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionToJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import fr.urssaf.image.sae.utils.CompareUtils;
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
      jobExeToJobStepDao.saveWithMapper(jobcql);
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


  //############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobExecutionsToStep() {

    // liste venant de la base thrift après transformation
    final List<JobExecutionToJobStepCql> listJobThrift = getListJobExeToStepThrift();

    // liste venant de la base cql
    final List<JobExecutionToJobStepCql> listJobCql = new ArrayList<>();
    final Iterator<JobExecutionToJobStepCql> it = jobExeToJobStepDao.findAllWithMapper();
    while (it.hasNext()) {
      final JobExecutionToJobStepCql jobExToJR = it.next();        
      listJobCql.add(jobExToJR);	    	
    }

    // comparaison de deux listes
    final boolean isBaseEq = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isBaseEq) {
      LOG.info("MIGRATION_JobExecutions -- Les listes metadata sont identiques");
    } else {
      LOG.warn("MIGRATION_JobExecutions -- ATTENTION: Les listes metadata sont différentes ");
    }

    return isBaseEq;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * @return
   */
  public List<JobExecutionToJobStepCql> getListJobExeToStepThrift(){

    final List<JobExecutionToJobStepCql> listJobThrift = new ArrayList<>();

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBEXECUTION_TO_JOBSTEP_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final Long jobExecutionId = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      final Long idStep = row.getLong("column1");

      final JobExecutionToJobStepCql jobcql = new JobExecutionToJobStepCql();
      jobcql.setJobStepId(idStep);
      jobcql.setJobExecutionId(jobExecutionId);
      // la value n'est pas renseignée dans l'ancien système
      jobcql.setValue("");
      listJobThrift.add(jobcql);
    }

    return listJobThrift;
  }


}
