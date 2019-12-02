/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.piletravaux;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.pile.travaux.dao.cql.IJobRequestDaoCql;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;
import fr.urssaf.image.sae.pile.travaux.support.JobRequestSupport;
import fr.urssaf.image.sae.pile.travaux.utils.JobRequestMapper;
import fr.urssaf.image.sae.piletravaux.dao.IGenericJobTypeDao;
import fr.urssaf.image.sae.piletravaux.model.GenericJobType;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class MigrationJobRequest implements IMigration {

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationJobRequest.class);

  @Autowired
  IGenericJobTypeDao genericdao;

  @Autowired
  IJobRequestDaoCql cqldao;

  @Autowired
  JobRequestSupport jobRequestSupport;


  // @Qualifier("CassandraClientFactory")
  @Autowired
  private CassandraClientFactory ccf;

  // String keyspace = "SAE";

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationJobHistory - migrationFromThriftToCql - start ");

    // extraction des colonnes dans la table thrift
    final Iterator<GenericJobType> it = genericdao.findAllByCFName("JobHistory", ccf.getKeyspace().getKeyspaceName());

    UUID lastKey = null;
    if (it.hasNext()) {
      final Row row = (Row) it.next();
      lastKey = UUIDSerializer.get().fromByteBuffer(row.getBytes("key"));
    }

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

        // <UUID, String> le type de clé et le nom de la colonne
        final ColumnFamilyResult<UUID, String> result = jobRequestSupport.getJobRequestTmpl().queryColumns(lastKey);

        // Conversion en objet JobRequest
        final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(result);

        final JobRequestCql jobcql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(jobRequest);
        // enregistrement
        cqldao.saveWithMapper(jobcql);

        // réinitialisation
        lastKey = key;

        nb++;
      }

      // dernier cas
      final ColumnFamilyResult<UUID, String> result = jobRequestSupport.getJobRequestTmpl().queryColumns(lastKey);
      // Conversion en objet JobRequest
      final JobRequest jobRequest = jobRequestSupport.createJobRequestFromResult(result);
      final JobRequestCql jobcql = JobRequestMapper.mapJobRequestThriftToJobRequestCql(jobRequest);
      // enregistrement
      cqldao.saveWithMapper(jobcql);

    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobHistory - migrationFromThriftToCql - end");

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationJobHistory - migrationFromCqlToThrift start ");

    final Iterator<JobRequestCql> it = cqldao.findAllWithMapper();
    int nb = 0;
    while (it.hasNext()) {
      final JobRequest jobcql = JobRequestMapper.mapJobRequestCqlToJobRequestThrift(it.next());
      final UUID idJob = jobcql.getIdJob();

      final JobToCreate job = new JobToCreate();
      job.setIdJob(idJob);
      job.setType(jobcql.getType());
      job.setJobParameters(jobcql.getJobParameters());
      job.setClientHost(jobcql.getClientHost());
      job.setDocCount(jobcql.getDocCount());
      job.setSaeHost(jobcql.getSaeHost());
      job.setCreationDate(jobcql.getCreationDate());
      job.setDocCountTraite(jobcql.getDocCountTraite());
      job.setJobKey(jobcql.getJobKey());
      job.setParameters(job.getParameters());
      job.setVi(jobcql.getVi());

      final Date date = new Date();
      jobRequestSupport.ajouterJobDansJobRequest(job, date.getTime());
      nb++;
    }

    LOGGER.debug(" Totale : " + nb);
    LOGGER.debug(" MigrationJobHistory - migrationFromCqlToThrift end");

  }

}
