package fr.urssaf.image.sae.pile.travaux.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.pile.travaux.dao.JobRequestDao;
import fr.urssaf.image.sae.pile.travaux.dao.JobsQueueDao;
import fr.urssaf.image.sae.pile.travaux.dao.iterator.JobQueueIterator;
import fr.urssaf.image.sae.pile.travaux.dao.serializer.JobQueueSerializer;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.service.JobLectureService;

/**
 * Implémentation du service {@link JobLectureService}
 * 
 * 
 */
@Service
public class JobLectureImpl implements JobLectureService {

   private final JobRequestDao jobRequestDao;

   private final JobsQueueDao jobsQueueDao;

   /**
    * Valeur de la clé pour les jobs en attente de réservation
    */
   private static final String JOBS_WAITING_KEY = "jobsWaiting";

   /**
    * 
    * @param jobRequestDao
    *           DAO de {@link JobRequest}
    * @param jobsQueueDao
    *           DAO de {@link jobsQueueDao}
    */
   @Autowired
   public JobLectureImpl(JobRequestDao jobRequestDao, JobsQueueDao jobsQueueDao) {

      this.jobRequestDao = jobRequestDao;
      this.jobsQueueDao = jobsQueueDao;

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

}
