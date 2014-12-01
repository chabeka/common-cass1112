package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.utils.TimeUUIDUtils;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.pile.travaux.dao.JobHistoryDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.dao.iterator.JobQueueIterator;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;

/**
 * Implémentation du service {@link JobLectureService}
 * 
 * 
 */
@Service
public class JobLectureServiceImpl implements JobLectureService {

   private static final int MAX_ALL_JOBS = 200;

   private final JobRequestDao jobRequestDao;

   private final JobsQueueDao jobsQueueDao;

   private final JobHistoryDao jobHistoryDao;

   private static final int MAX_JOB_ATTIBUTS = 100;

   /**
    * Valeur de la clé pour les jobs en attente de réservation
    */
   private static final String JOBS_WAITING_KEY = "jobsWaiting";

   /**
    * 
    * @param jobRequestDao
    *           DAO de {@link JobRequest}
    * @param jobsQueueDao
    *           DAO de {@link JobQueue}
    * @param jobHistoryDao
    *           DAO de {@link JobHistory}
    */
   @Autowired
   public JobLectureServiceImpl(JobRequestDao jobRequestDao,
         JobsQueueDao jobsQueueDao, JobHistoryDao jobHistoryDao) {

      this.jobRequestDao = jobRequestDao;
      this.jobsQueueDao = jobsQueueDao;
      this.jobHistoryDao = jobHistoryDao;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final JobRequest getJobRequest(UUID jobRequestUUID) {

      // Requête dans Cassandra
      ColumnFamilyResult<UUID, String> result = this.jobRequestDao
            .getJobRequestTmpl().queryColumns(jobRequestUUID);

      // Conversion en objet JobRequest
      JobRequest jobRequest = jobRequestDao.createJobRequestFromResult(result);

      return jobRequest;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Iterator<JobQueue> getUnreservedJobRequestIterator() {

      SliceQuery<String, UUID, String> sliceQuery = jobsQueueDao
            .createSliceQuery();
      sliceQuery.setKey(JOBS_WAITING_KEY);

      return new JobQueueIterator(sliceQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobQueue> getNonTerminatedSimpleJobs(String hostname) {

      ColumnFamilyResult<String, UUID> result = jobsQueueDao.getJobsQueueTmpl()
            .queryColumns(hostname);
      Collection<UUID> colNames = result.getColumnNames();
      List<JobQueue> list = new ArrayList<JobQueue>(colNames.size());
      JobQueueSerializer serializer = JobQueueSerializer.get();
      for (UUID uuid : colNames) {
         JobQueue jobQueue = serializer.fromBytes(result.getByteArray(uuid));
         list.add(jobQueue);
      }
      return list;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> getNonTerminatedJobs(String hostname) {

      List<JobQueue> jobQueues = this.getNonTerminatedSimpleJobs(hostname);

      List<JobRequest> jobRequests = new ArrayList<JobRequest>();

      for (JobQueue jobQueue : jobQueues) {

         JobRequest jobRequest = this.getJobRequest(jobQueue.getIdJob());
         jobRequests.add(jobRequest);
      }

      return jobRequests;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobHistory> getJobHistory(UUID idJob) {

      ColumnFamilyResult<UUID, UUID> result = jobHistoryDao.getJobHistoryTmpl()
            .queryColumns(idJob);

      Collection<UUID> colNames = result.getColumnNames();
      List<JobHistory> histories = new ArrayList<JobHistory>(colNames.size());
      StringSerializer serializer = StringSerializer.get();
      for (UUID timeUUID : colNames) {

         JobHistory jobHistory = new JobHistory();

         jobHistory.setTrace(serializer
               .fromBytes(result.getByteArray(timeUUID)));
         jobHistory.setDate(new Date(TimeUUIDUtils.getTimeFromUUID(timeUUID)));

         histories.add(jobHistory);

      }
      return histories;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> getAllJobs(Keyspace keyspace) {
      return getAllJobs(keyspace, MAX_ALL_JOBS);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<JobRequest> getAllJobs(Keyspace keyspace, int maxKeysToRead) {

      // On n'utilise pas d'index. On récupère tous les jobs sans distinction,
      // en requêtant directement dans la CF JobRequest
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<UUID, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(keyspace, UUIDSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(JobRequestDao.JOBREQUEST_CFNAME);
      rangeSlicesQuery.setRange("", "", false, MAX_JOB_ATTIBUTS);
      rangeSlicesQuery.setRowCount(maxKeysToRead);
      QueryResult<OrderedRows<UUID, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<UUID, String, byte[]> converter = new QueryResultConverter<UUID, String, byte[]>();
      ColumnFamilyResultWrapper<UUID, String> result = converter
            .getColumnFamilyResultWrapper(queryResult, UUIDSerializer.get(),
                  StringSerializer.get(), bytesSerializer);

      // On itère sur le résultat
      HectorIterator<UUID, String> resultIterator = new HectorIterator<UUID, String>(
            result);
      List<JobRequest> list = new ArrayList<JobRequest>();
      for (ColumnFamilyResult<UUID, String> row : resultIterator) {
         JobRequest jobRequest = jobRequestDao.createJobRequestFromResult(row);
         // On peut obtenir un jobRequest null dans le cas d'un jobRequest
         // effacé
         if (jobRequest != null)
            list.add(jobRequest);
      }
      return list;

   }

   /**
    * {@inheritDoc}
    */
   public final boolean isJobResettable(JobRequest job) {
      String state = job.getState().toString();
      if (state.equals("RESERVED") || state.equals("STARTING")) {
         return true;
      }
      return false;
   }

   /**
    * {@inheritDoc}
    */
   public final boolean isJobRemovable(JobRequest job) {
      String state = job.getState().toString();
      if ("CREATED".equals(state) || "STARTING".equals(state)
            || "RESERVED".equals(state)) {
         return true;
      }
      return false;
   }

}
