package fr.urssaf.image.sae.services.batch;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobExecutionDao;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraJobInstanceDao;
import fr.urssaf.image.commons.cassandra.spring.batch.dao.CassandraStepExecutionDao;
import fr.urssaf.image.sae.services.batch.exception.JobInexistantException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-batch-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementAsynchroneServiceTest {

   @Autowired
   private TraitementAsynchroneService service;

   @Autowired
   private CassandraJobInstanceDao jobInstanceDao;

   @Autowired
   private CassandraJobExecutionDao jobExecutionDao;

   @Autowired
   private CassandraStepExecutionDao stepExecutionDao;

   private long idJob;

   @Before
   public void before() {

      idJob = 0;
   }

   @After
   public void after() {

      // suppression du traitement dde masse
      if (jobInstanceDao.getJobInstance(idJob) != null) {

         jobInstanceDao.deleteJobInstance(idJob, jobExecutionDao,
               stepExecutionDao);

      }
   }

   @Test
   public void ajouterJobCaptureMasse_success() {

      UUID uuid = UUID.randomUUID();
      idJob = service.ajouterJobCaptureMasse("url_ecde", uuid);

      JobInstance jobInstance = jobInstanceDao.getJobInstance(idJob);

      Assert.assertNotNull("le traitement " + idJob + " doit être créé",
            jobInstance);

      JobParameters jobParameters = jobInstance.getJobParameters();

      Assert
            .assertEquals(
                  "le nombre de paramètres du traitement de la capture en masse est incorrect",
                  2, jobParameters.getParameters().size());

      String parameter1 = "capture.masse.sommaire";

      Assert.assertEquals("la valeur du paramètre " + parameter1, "url_ecde",
            jobParameters.getString(parameter1));

      String parameter2 = "capture.masse.idtraitement";

      Assert.assertEquals("la valeur du paramètre " + parameter2, uuid
            .toString(), jobParameters.getString(parameter2));

   }

   @Test
   public void lancerJob_success() throws JobInexistantException {

      // création d'un traitement de capture en masse
      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters jobParameters = new JobParameters(parameters);
      JobInstance jobInstance = jobInstanceDao.createJobInstance(
            "capture_masse", jobParameters);

      idJob = jobInstance.getId();

      String exitCode = service.lancerJob(jobInstance.getId());
      Assert.assertEquals("le code de sortie du traitement est inattendu",
            "COMPLETED", exitCode);

   }

   @Test
   public void lancerJob_failure_jobInexistantException()
         throws JobInexistantException {

      idJob = 1;

      // on s'assure que le traitement n'existe pas!
      if (jobInstanceDao.getJobInstance(idJob) != null) {

         jobInstanceDao.deleteJobInstance(idJob, jobExecutionDao,
               stepExecutionDao);

      }

      try {

         service.lancerJob(idJob);

         Assert
               .fail("Une exception JobInexistantException doit être levée pour le traitement "
                     + idJob);

      } catch (JobInexistantException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Impossible de lancer le traitement n°" + idJob
                     + " car il n'existe pas.", e.getMessage());

         Assert.assertEquals("l'instance du job est incorrect", idJob, e
               .getInstanceId());

      }
   }
}
