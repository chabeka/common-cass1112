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

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceToJobExecutionCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceToJobExecutionDaoCql;
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
 * Classe permettant de faire la migration de données de la table {@link JobinstanceToJobExecution}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobinstanceToJobExecution extends MigrationJob implements IMigration {

  private static final Logger LOG = LoggerFactory.getLogger(MigrationJobinstanceToJobExecution.class);

  @Autowired
  IJobInstanceToJobExecutionDaoCql jobInstToJobEx;

  @Override
  public void migrationFromThriftToCql() {

    LOG.info(" MigrationJobinstanceToJobExecution - migrationFromThriftToCql - DEBUT ");


    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    final int nb = 0;
    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final Long idInst = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      final Long idJobEx = row.getLong("column1");
      final JobInstanceToJobExecutionCql jobInstToJ = new JobInstanceToJobExecutionCql();
      jobInstToJ.setJobInstanceId(idInst);
      jobInstToJ.setJobExecutionId(idJobEx);
      jobInstToJ.setValue("");
      jobInstToJobEx.saveWithMapper(jobInstToJ);
    }

    LOG.info(" MigrationJobinstanceToJobExecution - migrationFromThriftToCql - FIN   ");
    LOG.info(" MigrationJobinstanceToJobExecution - migrationFromThriftToCql - Total:{}    ", nb);

  }

  @Override
  public void migrationFromCqlTothrift() {


    LOG.info(" MigrationJobinstanceToJobExecution - migrationFromCqlTothrift - DEBUT ");


    final Serializer<String> sSlz = StringSerializer.get();
    final Serializer<byte[]> bSlz = BytesArraySerializer.get();
    final Serializer<Long> lSlz = LongSerializer.get();
    final byte[] empty = new byte[0];
    ;
    final Mutator<byte[]> mutator = HFactory.createMutator(ccfthrift.getKeyspace(), bSlz);

    final Iterator<JobInstanceToJobExecutionCql> it = jobInstToJobEx.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      final JobInstanceToJobExecutionCql jobInstToJ = it.next();
      final Long instId = jobInstToJ.getJobInstanceId();
      final Long exeId = jobInstToJ.getJobExecutionId();

      mutator.addInsertion(lSlz.toBytes(instId),
                           Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME,
                           HFactory.createColumn(exeId, empty, lSlz, bSlz));
      mutator.execute();
      nb++;
    }


    LOG.info(" MigrationJobinstanceToJobExecution - migrationFromCqlTothrift - FIN   ");
    LOG.info(" MigrationJobinstanceToJobExecution - migrationFromCqlTothrift - Total:{}    ", nb);


  }


  //############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobInstanceToExecution() {

    // liste d'objet cql venant de la base thrift après transformation
    final List<JobInstanceToJobExecutionCql> listJobThrift = getListJobInstanceToJobExecutionThrift();

    // liste venant de la base cql
    final List<JobInstanceToJobExecutionCql> listJobCql = new ArrayList<>();
    final Iterator<JobInstanceToJobExecutionCql> it = jobInstToJobEx.findAllWithMapper();
    while (it.hasNext()) {
      final JobInstanceToJobExecutionCql jobExToJR = it.next();        
      listJobCql.add(jobExToJR);	    	
    }

    // comparaison de deux listes
    final boolean isListEq = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isListEq) {
      LOG.info("MIGRATION_JobInstanceToExecution -- Les listes JobInstanceToExecution sont identiques  , nb=" + listJobThrift.size());
    } else {
      LOG.warn("MIGRATION_JobInstanceToExecution -- ATTENTION: Les listes JobInstanceToExecution sont différentes ");
    }

    return isListEq;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * @return
   */
  public List<JobInstanceToJobExecutionCql> getListJobInstanceToJobExecutionThrift(){

    final List<JobInstanceToJobExecutionCql> listJobThrift = new ArrayList<>();

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());

    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final Long idInst = LongSerializer.get().fromByteBuffer(row.getBytes("key"));
      final Long idJobEx = row.getLong("column1");
      final JobInstanceToJobExecutionCql jobInstToJ = new JobInstanceToJobExecutionCql();
      jobInstToJ.setJobInstanceId(idInst);
      jobInstToJ.setJobExecutionId(idJobEx);
      jobInstToJ.setValue("");
      listJobThrift.add(jobInstToJ);
    }

    return listJobThrift;
  }
}
