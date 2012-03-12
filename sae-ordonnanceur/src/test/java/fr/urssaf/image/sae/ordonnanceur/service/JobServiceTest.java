package fr.urssaf.image.sae.ordonnanceur.service;

import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobExecutionDao;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobInstanceDao;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraStepExecutionDao;
import fr.urssaf.image.sae.ordonnanceur.exception.JobDejaReserveException;
import fr.urssaf.image.sae.ordonnanceur.exception.JobInexistantException;
import fr.urssaf.image.sae.ordonnanceur.util.HostUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-cassandra-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
@DirtiesContext
public class JobServiceTest {

   @Autowired
   private JobService jobService;

   @Autowired
   private CassandraJobInstanceDao jobInstanceDao;

   @Autowired
   private CassandraJobExecutionDao jobExecutionDao;

   @Autowired
   private CassandraStepExecutionDao stepExecutionDao;

   private JobInstance jobInstance;

   @Before
   public void before() {

      // création d'un traitement

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters jobParameters = new JobParameters(parameters);
      jobInstance = jobInstanceDao.createJobInstance("jobTest", jobParameters);
   }

   @After
   public void after() {

      // suppression du traitement
      if (jobInstance != null
            && jobInstanceDao.getJobInstance(jobInstance.getId()) != null) {

         jobInstanceDao.deleteJobInstance(jobInstance.getId(), jobExecutionDao,
               stepExecutionDao);
      }
   }

   @Test
   public void recupJobEnCours_success() {

      JobExecution jobExecution = new JobExecution(jobInstance);
      jobExecutionDao.saveJobExecution(jobExecution);

      // récupération des traitements en cours
      Collection<JobExecution> jobEnCours = jobService.recupJobEnCours();

      Assert.assertTrue("la liste des job en cours doit être non vide",
            !jobEnCours.isEmpty());

   }

   @Test
   public void recupJobsALancer_success() {

      // récupération des traitements à lancer
      List<JobInstance> jobsAlancer = jobService.recupJobsALancer();

      Assert.assertTrue("la liste des job à lancer doit être non vide",
            !jobsAlancer.isEmpty());

   }

   @Test
   public void reserveJob_success() throws JobInexistantException,
         JobDejaReserveException, UnknownHostException {

      long idJob = jobInstance.getId();
      jobService.reserveJob(idJob);

      String reservingServer = jobInstanceDao.getReservingServer(idJob);

      String serverName = HostUtils.getLocalHostName();

      Assert.assertEquals("le traitement doit être réservé", serverName,
            reservingServer);

   }

   @Test
   public void reserveJob_failure_jobInexistantException()
         throws JobDejaReserveException {

      long idJob = jobInstance.getId();

      // on s'assure que le traitement n'existe pas!
      jobInstanceDao.deleteJobInstance(jobInstance.getId(), jobExecutionDao,
            stepExecutionDao);

      try {
         jobService.reserveJob(idJob);

         Assert
               .fail("une exception de type JobInexistantException doit être levée");

      } catch (JobInexistantException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

      }

   }

   @Test
   public void reserveJob_failure_jobDejaReserveException()
         throws JobDejaReserveException, UnknownHostException,
         JobInexistantException {

      long idJob = jobInstance.getId();

      // réserve une premiere fois le traitement
      jobService.reserveJob(idJob);

      try {
         // réserve une seconde fois le même fois le traitement
         jobService.reserveJob(idJob);

         Assert
               .fail("une exception de type JobDejaReserveException doit être levée");

      } catch (JobDejaReserveException e) {

         String reservingServer = jobInstanceDao.getReservingServer(idJob);

         Assert.assertEquals("le message de l'exception est inattendu",
               "Le traitement n°" + idJob
                     + " est déjà réservé par le serveur '" + reservingServer
                     + "'.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

         Assert.assertEquals("le nom du serveur est incorrect",
               reservingServer, e.getServer());

      }

   }
}
