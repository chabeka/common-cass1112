package fr.urssaf.image.sae.jobspring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.jobspring.model.GenericJobSpring;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;

/**
 * Classe permettant de faire la migration de données de la table {@link JobinstancesByName}
 * de thrift vers cql ou de cql vers thrift
 */
@Component
public class MigrationJobInstancesByName extends MigrationJob implements IMigration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobInstancesByName.class);

  @Autowired
  IJobInstancesByNameDaoCql jobInstByNamedao;

  protected static final String JOBINSTANCES_BY_NAME_CFNAME = "JobInstancesByName";

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromThriftToCql() {

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBINSTANCES_BY_NAME_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (!"_unreserved".equals(key)) {
        final Long id = row.getLong("column1");
        final String value = StringSerializer.get().fromByteBuffer(row.getBytes("value"));
        final JobInstancesByNameCql jobcql = new JobInstancesByNameCql();
        jobcql.setJobInstanceId(id);
        jobcql.setJobName(key);
        jobInstByNamedao.saveWithMapper(jobcql);
      } else {

      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromCqlTothrift() {
    final Iterator<JobInstancesByNameCql> it = jobInstByNamedao.findAllWithMapper();
    final ColumnFamilyTemplate<String, Long> jobInstancesByNameTemplate = new ThriftColumnFamilyTemplate<>(
        ccfthrift.getKeyspace(),
        JOBINSTANCES_BY_NAME_CFNAME,
        StringSerializer.get(),
        LongSerializer.get());
    while (it.hasNext()) {
      final JobInstancesByNameCql job = it.next();
      final ColumnFamilyUpdater<String, Long> updater2 = jobInstancesByNameTemplate.createUpdater(job.getJobName());
      updater2.setByteArray(job.getJobInstanceId(), new byte[0]);
      jobInstancesByNameTemplate.update(updater2);
    }

  }

  //############################################################
  // ################# TESTDES DONNEES ######################
  // ############################################################

  public boolean compareJobInstanceByName() {

    // liste venant de la base thrift après transformation
    final List<JobInstancesByNameCql> listJobThrift = getListJobInstanceByNameThrift();

    // liste venant de la base cql
    final List<JobInstancesByNameCql> listJobCql = new ArrayList<>();
    final Iterator<JobInstancesByNameCql> it = jobInstByNamedao.findAllWithMapper();
    while (it.hasNext()) {
      final JobInstancesByNameCql jobExToJR = it.next();        
      listJobCql.add(jobExToJR);	    	
    }

    // comparaison de deux listes
    final boolean isListEqual = CompareUtils.compareListsGeneric(listJobCql, listJobThrift);
    if (isListEqual) {
      LOGGER.info("MIGRATION_JobInstancesByNameCql -- Les listes metadata sont identiques, nb=" + listJobThrift.size());
    } else {
      LOGGER.warn("MIGRATION_JobInstancesByNameCql -- ATTENTION: Les listes metadata sont différentes ");
    }

    return isListEqual;
  }

  /**
   * Liste des job cql venant de la table thirft après transformation
   * @return
   */
  public List<JobInstancesByNameCql> getListJobInstanceByNameThrift(){

    final List<JobInstancesByNameCql> listJobThrift = new ArrayList<>();

    final Iterator<GenericJobSpring> it = genericdao.findAllByCFName(Constante.JOBINSTANCES_BY_NAME_CFNAME, ccfthrift.getKeyspace().getKeyspaceName());
    while (it.hasNext()) {
      final Row row = (Row) it.next();
      final String key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (!"_unreserved".equals(key)) {
        final Long id = row.getLong("column1");
        // La value n'est pas setter dans l'ancien système
        final String value = "";
        final JobInstancesByNameCql jobcql = new JobInstancesByNameCql();
        jobcql.setJobInstanceId(id);
        jobcql.setJobName(key);
        listJobThrift.add(jobcql);
      }
    }

    return listJobThrift;
  }
}
