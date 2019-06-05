/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.daocql.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstanceCql;
import fr.urssaf.image.commons.cassandra.spring.batch.cqlmodel.JobInstancesByNameCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstanceDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobInstancesByNameDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.daocql.IJobStepExecutionDaoCql;
import fr.urssaf.image.commons.cassandra.spring.batch.helper.CassandraJobHelper;
import fr.urssaf.image.commons.cassandra.spring.batch.idgenerator.IdGenerator;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobParametersCodec;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.JobTranslateUtils;
import fr.urssaf.image.sae.commons.context.BytesBlobCodec;
import fr.urssaf.image.sae.commons.dao.impl.GenericDAOImpl;

/**
 * TODO (AC75095028) Description du type
 */
@Repository
public class JobInstanceDaoCqlImpl extends GenericDAOImpl<JobInstanceCql, Long> implements IJobInstanceDaoCql {

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
      ccf.getCluster().getConfiguration().getCodecRegistry().register(BytesBlobCodec.instance);
      ccf.getCluster().getConfiguration().getCodecRegistry().register(JobParametersCodec.instance);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int countJobInstances(final String name) {
      return this.count();
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
    *           Id de l'instance à supprimer
    * @param executionDao
    *           DAO permettant de supprimer les executions de l'instance
    * @param stepExecutionDao
    *           DAO permettant de supprimer les steps de l'instance
    */
   @Override
   public final void deleteJobInstance(final Long instanceId) {
      Assert.notNull(instanceId, "JobInstanceId cannot be null.");

      final JobInstance jobInstance = getJobInstance(instanceId);
      if (jobInstance != null) {
         // Suppression dans JobInstance
         this.deleteById(instanceId);
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
      final Optional<JobInstanceCql> opt = this.findWithMapperById(instanceId);
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

      // TODO orde de recuperation des instances

      // On se sert de JobInstancesByName, dont la clé est jobName, pour
      // récupérer les id des jobInstances
      final Iterator<JobInstancesByNameCql> it = jobIByNamedaocql.findAllWithMapperById(jobName);
      final List<Long> jobIds = new ArrayList<Long>(count);
      while (it.hasNext()) {
         final JobInstancesByNameCql job = it.next();
         jobIds.add(job.getJobInstanceId());
      }

      // recuperation des JobInstance correspondant au jobIds
      final List<JobInstance> jobInstList = new ArrayList<JobInstance>();
      final Iterator<JobInstanceCql> itJobInst = this.findAllWithMapper();
      int compteur = 0;
      while (itJobInst.hasNext()) {
         // test sur le compte pour être sur que la liste
         // de JobInstance renvoyé ne soit pas superieur à count
         if (compteur >= start + count) {
            break;
         }
         //
         if (compteur >= start) {
            final JobInstanceCql jobInst = itJobInst.next();
            if (jobIds.contains(jobInst.getJobInstanceId())) {
               jobInstList.add(JobTranslateUtils.getJobInstanceToJobInstanceCql(jobInst));
            }
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
      final ArrayList<String> list = new ArrayList<String>();
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
    *           Id de l'instance de job à réserver
    * @param serverName
    *           Nom du serveur qui réserve le job
    */
   @Override
   public final void reserveJob(final long instanceId, final String serverName) {
      Assert.notNull(instanceId, "Job instance id name must not be null.");
      Assert.notNull(serverName,
                     "serverName must not be null (but can by empty)");

      // reservation du job
      final Optional<JobInstanceCql> opt = this.findWithMapperById(instanceId);
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
    *           Id de l'instance
    * @return Nom du serveur qui réserve l'instance de job, ou vide si aucun serveur
    *         ne réserve le job, ou null si l'instance n'existe pas
    */
   @Override
   public final String getReservingServer(final long instanceId) {
      Assert.notNull(instanceId, "Job instance id name must not be null.");

      String serverName = null;

      final Optional<JobInstanceCql> opt = this.findWithMapperById(instanceId);
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

      final List<JobInstance> listJobIThrift = new ArrayList<JobInstance>();
      while (it.hasNext()) {
         final JobInstanceCql jobcql = it.next();
         listJobIThrift.add(JobTranslateUtils.getJobInstanceToJobInstanceCql(jobcql));
      }
      return listJobIThrift;
   }

}
