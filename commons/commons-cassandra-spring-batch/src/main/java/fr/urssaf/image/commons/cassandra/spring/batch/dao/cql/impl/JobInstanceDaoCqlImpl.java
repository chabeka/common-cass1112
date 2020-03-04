/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.CodecNotFoundException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.cql.codec.BytesBlobCodec;
import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobExecutionToJobStepDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.cql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobParametersCodec;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;

/**
 * Classe implémentant le DAO  {@link IJobInstanceDaoCql}
 */
@Repository
public class JobInstanceDaoCqlImpl extends GenericDAOImpl<JobInstanceCql, Long> implements IJobInstanceDaoCql {


  private static final Logger LOGGER = LoggerFactory.getLogger(JobInstanceDaoCqlImpl.class);
  /**
   * @param ccf
   */
  @Autowired
  public JobInstanceDaoCqlImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

  /**
   *
   */
  private static final String RESERVEDBY = "reservedby";

  /**
   * TODO (AC75095028) Description du champ
   */

  private static final String JOBKEY = "jobkey";

  @Autowired
  @Qualifier("jobinstanceidgeneratorcql")
  private IdGenerator idGenerator;

  private static final int MAX_ROWS = 500;

  @Autowired
  private IJobInstancesByNameDaoCql jobIByNamedaocql;

  @Autowired
  private IJobStepExecutionDaoCql stepdao;

  @Autowired
  private IJobExecutionDaoCql executiondao;

  /**
   * Cette methode est appelé après l'instanciation de la classe par spring.
   * Grace à l'annotation {@link PostConstruct} on est sur que les dependances
   * son bien injectés ({@link CassandraClientFactory}) et cela nous permet d'enregistrer tous les <b>codec</b> nécessaires
   * aux opérations sur la table (CF) de ce DAO
   */
  @PostConstruct
  public void setRegister() {
    if(ccf != null) {
      final CodecRegistry registry = ccf.getCluster().getConfiguration().getCodecRegistry();
      registerCodecIfNotFound(registry, BytesBlobCodec.instance);
      registerCodecIfNotFound(registry, JobParametersCodec.instance);

      // ccf.getCluster().getConfiguration().getCodecRegistry().register(BytesBlobCodec.instance);
      // ccf.getCluster().getConfiguration().getCodecRegistry().register(JobParametersCodec.instance);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countJobInstances(final String name) {
    return count();
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public JobInstance createJobInstance(final String jobName, final JobParameters jobParameters) {
	
    Assert.notNull(jobName, "Job name must not be null.");
    Assert.notNull(jobParameters, "JobParameters must not be null.");

    Assert.state(getJobInstance(jobName, jobParameters) == null, "JobInstance must not already exist");

    final long jobId = idGenerator.getNextId();
    final JobInstance instance = new JobInstance(jobId, jobParameters, jobName);
    instance.incrementVersion();
    saveJobInstance(instance);

    return instance;
  }

  /**
   * Enregistre une jobInstance dans cassandra et ajout tous les liens associé à la CF
   *
   * @param instance
   *          job dans job instance
   */
  private void saveJobInstance(final JobInstance instance) {

    // On enregistre l'instance dans JobInstance
    final JobInstanceCql job = JobTranslateUtils.getJobInstanceCqlToJobInstance(instance);
    this.saveWithMapper(job);
    // Ajout de l'index dans jobInstancesByName
    final JobInstancesByNameCql jobInstByName = JobTranslateUtils.getJobInstancesByNameCqlToJobInstance(job);
    jobIByNamedaocql.saveWithMapper(jobInstByName);
  }

  /**
   * Supprime une instance de Job de la base cassandra, ainsi que les entités
   * jobExecution et stepExecution associées
   *
   * @param instanceId
   *          Id de l'instance à supprimer
   */
  @Override
  public final void deleteJobInstance(final Long instanceId) {
    Assert.notNull(instanceId, "JobInstanceId cannot be null.");

    final JobInstance jobInstance = getJobInstance(instanceId);
    if (jobInstance != null) {
      // Suppression dans JobInstance
      deleteById(instanceId);
      // Suppression dans JobInstancesByName
      jobIByNamedaocql.deleteById(jobInstance.getJobName());
      // suppression des jobExecution
      executiondao.deleteJobExecutionsOfInstance(jobInstance, stepdao);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JobInstance getJobInstance(final String jobName, final JobParameters jobParameters) {
    Assert.notNull(jobName, "Job name must not be null.");
    Assert.notNull(jobParameters, "JobParameters must not be null.");

    final byte[] jobKey = CassandraJobHelper.createJobKey(jobName, jobParameters);
    final Select select = QueryBuilder.select().from(getTypeArgumentsName());
    select.where(QueryBuilder.eq(JOBKEY, jobKey));
    final JobInstanceCql jobInstanceCql = getMapper().map(getSession().execute(select)).one();
    final JobInstance inst = JobTranslateUtils.getJobInstanceToJobInstanceCql(jobInstanceCql);
    /*
     * final Iterator<JobInstanceCql> it = this.findAllWithMapper();
     * while (it.hasNext()) {
     * final JobInstanceCql jobCql = it.next();
     * if (jobCql.getJobKey().equals(jobKey)) {
     * return getJobInstanceToJobInstanceCql(jobCql);
     * }
     * }
     */
    return inst;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JobInstance getJobInstance(final Long instanceId) {
    final Optional<JobInstanceCql> opt = findWithMapperById(instanceId);
    if (opt.isPresent()) {
      return JobTranslateUtils.getJobInstanceToJobInstanceCql(opt.get());
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JobInstance getJobInstance(final JobExecution jobExecution) {
    // Récupération de l'id de l'instance à partir de l'id de l'exécution
    final JobExecution jobEx = executiondao.getJobExecution(jobExecution.getId());
    if (jobEx != null) {
      final Long instanceId = jobEx.getId();
      // Récupération de l'instance à partir de son id
      return getJobInstance(instanceId);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<JobInstance> getJobInstances(final String jobName, final int start, final int count) {
    
    List<JobInstanceCql> listJobInstance = findJobInstanceByName(jobName);
    Collections.sort(listJobInstance);
    
    int compteur = 0;
    final List<JobInstance> jobInstList = new ArrayList<>();
    
    for (JobInstanceCql jobInst : listJobInstance) {
        if (compteur >= start + count) {
          break;
        }
        //
        if (compteur >= start) {
            jobInstList.add(JobTranslateUtils.getJobInstanceToJobInstanceCql(jobInst));
         }        
        compteur++;
      }
    return jobInstList;
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public List<String> getJobNames() {

    final Iterator<JobInstancesByNameCql> listJobs = jobIByNamedaocql.findAllWithMapper();
    final ArrayList<String> list = new ArrayList<>();
    int i = 0;
    while (listJobs.hasNext()) {
      if (MAX_ROWS < i) {
        final JobInstancesByNameCql job = listJobs.next();
        list.add(job.getJobName());
      } else {
        break;
      }
      i++;
    }
    Collections.sort(list);
    return list;
  }

  /**
   * Réserve un job, c'est à dire inscrit un nom de serveur dans la colonne
   * "reservedBy" de l'instance de job
   *
   * @param instanceId
   *          Id de l'instance de job à réserver
   * @param serverName
   *          Nom du serveur qui réserve le job
   */
  @Override
  public final void reserveJob(final long instanceId, final String serverName) {
    Assert.notNull(instanceId, "Job instance id name must not be null.");
    Assert.notNull(serverName, "serverName must not be null (but can by empty)");

    // reservation du job
    final Optional<JobInstanceCql> opt = findWithMapperById(instanceId);
    if (opt.isPresent()) {
      final JobInstanceCql job = opt.get();
      // Création du champ "ReservedBy" et sauvegarde
      job.setReservedBy(serverName);
      this.saveWithMapper(job);

    }

  }

  /**
   * Renvoie le nom du serveur qui réserve l'instance de job
   *
   * @param instanceId
   *          Id de l'instance
   * @return Nom du serveur qui réserve l'instance de job, ou vide si aucun serveur
   *         ne réserve le job, ou null si l'instance n'existe pas
   */
  @Override
  public final String getReservingServer(final long instanceId) {
    Assert.notNull(instanceId, "Job instance id name must not be null.");

    String serverName = null;

    final Optional<JobInstanceCql> opt = findWithMapperById(instanceId);
    if (opt.isPresent()) {
      final JobInstanceCql jobI = opt.get();
      serverName = jobI.getReservedBy();
    }

    return serverName;
  }

  /**
   * Renvoie la liste des jobs non réservés
   *
   * @return Liste des jobs non réservés
   */
  @Override
  public final List<JobInstance> getUnreservedJobInstances() {

    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    select.where(QueryBuilder.eq(RESERVEDBY, Constante.UNRESERVED_KEY));
    final Iterator<JobInstanceCql> it = getMapper().map(getSession().execute(select)).iterator();

    final List<JobInstance> listJobIThrift = new ArrayList<>();
    while (it.hasNext()) {
      final JobInstanceCql jobcql = it.next();
      listJobIThrift.add(JobTranslateUtils.getJobInstanceToJobInstanceCql(jobcql));
    }
    return listJobIThrift;
  }
  
  /**
   * @return the logger
   */
  @Override
  public Logger getLogger() {
     return LOGGER;
  }
  
  private void registerCodecIfNotFound(final CodecRegistry registry, final TypeCodec<?> codec) {
    try {
      registry.codecFor(codec.getCqlType(), codec.getJavaType());
    }
    catch (final CodecNotFoundException e) {
      registry.register(codec);
    }
  }

@Override
public List<JobInstanceCql> findJobInstanceByName(String jobname) {
	final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    select.where(QueryBuilder.eq("jobname", jobname));
    List<JobInstanceCql> list = getMapper().map(getSession().execute(select)).all();
    return list;
}
}
