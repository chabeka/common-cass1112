package fr.urssaf.image.sae.pile.travaux.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.pile.travaux.exception.JobDejaReserveException;
import fr.urssaf.image.sae.pile.travaux.exception.JobInexistantException;
import fr.urssaf.image.sae.pile.travaux.exception.LockTimeoutException;
import fr.urssaf.image.sae.pile.travaux.model.JobHistory;
import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.model.JobToCreate;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-pile-travaux-Cassandra-local-test.xml" })
public class JobQueueServiceProcessExistingJobsTest {

   @Autowired
   private CassandraServerBean cassandraServer;
   
   @Autowired
   private JobQueueService jobQueueService;

   @Autowired
   private JobLectureService jobLectureService;
   
   private UUID idJobWithJobParam;
   
   @Before
   public void init()throws Exception, IOException,
   InterruptedException, ConfigurationException{
      // on s'assure que le job initialisé dans le fichier dataSet-avec-job-ase-pile-travaux.xml est bien chargé
      // On démarre un serveur cassandra local
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
   }
   
   @After
   public void end() throws Exception{
      cassandraServer.resetData();
   }

   /**
    * Test permettant de charger par le biais du fichier
    * dataSet-avec-job-sae-pile-travaux.xml un job avec un parametre à la place
    * du jobParameter. Après le chargement on ajout un nouveau job avec un job
    * parameter et on traite les deux jobs. les deux jobs doivent être traités
    * 
    * @throws JobDejaReserveException
    * @throws JobInexistantException
    * @throws LockTimeoutException
    */
   @Test
   public void processExistingJobWithOldParam() throws JobDejaReserveException, JobInexistantException, LockTimeoutException{

      UUID idJobExistant = UUID.fromString("3897da00-3893-11e2-9ff4-005056c00008");
      JobRequest jobRequest = jobLectureService.getJobRequest(idJobExistant);
      Assert.assertNotNull(jobRequest);
      // creation d'un nouveau job avec des job parameters
      idJobWithJobParam = TimeUUIDUtils.getUniqueTimeUUIDinMillis();
      Date dateCreation = new Date();

      Map<String,String> jobParam= new HashMap<String, String>();
      jobParam.put("parameters", "param");
      
      JobToCreate job = new JobToCreate();
      job.setIdJob(idJobWithJobParam);
      job.setType("ArchivageMasse");
      job.setJobParameters(jobParam);
      job.setClientHost("clientHost");
      job.setDocCount(100);
      job.setSaeHost("saeHost");
      job.setCreationDate(dateCreation);

      jobQueueService.addJob(job);
      
      //traitement des deux jobs
      
      jobQueueService.reserveJob(idJobExistant, "hostname", new Date());
      
      jobQueueService.reserveJob(idJobWithJobParam, "hostname", new Date());

      Date dateDebutTraitement = new Date();
      jobQueueService.startingJob(idJobExistant, dateDebutTraitement);
      
      jobQueueService.startingJob(idJobWithJobParam, dateDebutTraitement);

      //   réactualisation du job après la reservation et le démarrage 
      jobRequest = jobLectureService.getJobRequest(idJobExistant);
      
      // vérification de JobRequest avec le parameters
      Assert.assertEquals("l'état est inattendu", JobState.STARTING, jobRequest
            .getState());
      Assert.assertEquals("la date de démarrage est inattendue",
            dateDebutTraitement, jobRequest.getStartingDate());
      
      
      //vérification de JobRequest avec les jobParam
      JobRequest jobRequestJobParam = jobLectureService.getJobRequest(idJobWithJobParam);
      Assert.assertEquals("l'état est inattendu", JobState.STARTING, jobRequestJobParam
            .getState());
      Assert.assertEquals("la date de démarrage est inattendue",
            dateDebutTraitement, jobRequestJobParam.getStartingDate());

      // vérification de JobsQueues

      // rien à vérifier

      // vérification de JobHistory
      List<JobHistory> histories = jobLectureService.getJobHistory(idJobExistant);

      Assert.assertEquals("le nombre de message est inattendu", 2, histories
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DEMARRAGE DU JOB", histories.get(1).getTrace());
      
      // vérification de JobHistory
      List<JobHistory> historiesJobParam = jobLectureService.getJobHistory(idJobWithJobParam);

      Assert.assertEquals("le nombre de message est inattendu", 3, historiesJobParam
            .size());

      Assert.assertEquals(
            "le message de l'ajout d'un traitement est inattendu",
            "DEMARRAGE DU JOB", historiesJobParam.get(2).getTrace());
      

   }

}
