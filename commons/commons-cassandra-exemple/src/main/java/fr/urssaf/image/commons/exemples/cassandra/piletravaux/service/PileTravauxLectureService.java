package fr.urssaf.image.commons.exemples.cassandra.piletravaux.service;

import java.util.UUID;

import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;

import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobHistoryDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobQueuesDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.dao.JobRequestDao;
import fr.urssaf.image.commons.exemples.cassandra.piletravaux.modele.JobRequest;

public class PileTravauxLectureService {

   private static final Logger LOGGER = LoggerFactory.getLogger(PileTravauxLectureService.class);
   
   private Keyspace keyspace;
   
   private CuratorFramework curatorClient;
   
   private JobRequestDao jobRequestDao;
   
   private JobQueuesDao jobQueuesDao;
   
   private JobHistoryDao jobHistoryDao;
   
   private ColumnFamilyTemplate<String, String> jobQueuesTmpl;
   
   private ColumnFamilyTemplate<UUID, String> jobRequestTmpl;
   
   private ColumnFamilyTemplate<UUID, String> jobHistoryTmpl;
   
   
   /**
    * Valeur de la clé pour les jobs en attente de réservation
    */
   private static final String JOBS_WAITING_KEY = "jobsWaiting";
   
   
   
   public PileTravauxLectureService(
         Keyspace keyspace,
         CuratorFramework curatorClient,
         JobRequestDao jobRequestDao,
         JobQueuesDao jobQueuesDao,
         JobHistoryDao jobHistoryDao) {
      
      this.keyspace = keyspace;
      this.curatorClient = curatorClient;
      
      this.jobRequestDao = jobRequestDao;
      this.jobQueuesDao = jobQueuesDao;
      this.jobHistoryDao = jobHistoryDao;
      
      jobQueuesTmpl = jobQueuesDao.createCFTemplate(keyspace);
      jobRequestTmpl = jobRequestDao.createCFTemplate(keyspace);
      jobHistoryTmpl = jobHistoryDao.createCFTemplate(keyspace);
      
   }
   
   
   
   public JobRequest lireJob(
         UUID idJob) {
      
      // TODO: Traiter le cas du job qui n'existe pas
      
      // Requête dans Cassandra
      ColumnFamilyResult<UUID, String> result = jobRequestTmpl.queryColumns(idJob);
      
      // Conversion en objet JobRequest
      return jobRequestDao.createJobRequestFromResult(result);
      
   }
   
   
   
}
