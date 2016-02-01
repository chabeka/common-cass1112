package fr.urssaf.image.sae.ordonnanceur.service.impl;

import java.util.Set;
import java.util.UUID;

import junit.framework.Assert;

import org.apache.commons.lang.exception.NestableException;
import org.junit.Test;

import fr.urssaf.image.sae.ordonnanceur.service.impl.JobFailureServiceImpl.JobFailureInfo;

@SuppressWarnings("PMD.MethodNamingConventions")
public class JobFailureServiceImplTest {

   private final JobFailureServiceImpl jobFailureService = new JobFailureServiceImpl();

   @Test
   public void ajouterEchec_success() {

      UUID idJob1 = UUID.randomUUID();
      UUID idJob2 = UUID.randomUUID();
      UUID idJob3 = UUID.randomUUID();

      ajouterEchec(idJob1, 1);
      ajouterEchec(idJob1, 2);

      ajouterEchec(idJob2, 1);

      ajouterEchec(idJob3, 1);
      ajouterEchec(idJob3, 2);
      ajouterEchec(idJob3, 3);
      ajouterEchec(idJob3, 4);

      ajouterEchec(idJob1, 3);

      ajouterEchec(idJob3, 5);

      Set<UUID> jobsFailure = jobFailureService.findJobEchec();

      Assert.assertEquals("le nombre de traitements en échec est incorrect", 2,
            jobsFailure.size());

      Assert.assertFalse("le traitement idJob2 n°" + idJob2
            + " n'est pas attendu parmi les échecs", jobsFailure
            .contains(idJob2));

      Assert.assertTrue("le traitement idJob1 n°" + idJob1
            + " est attendu parmi les échecs", jobsFailure.contains(idJob1));

      Assert.assertTrue("le traitement idJob3 n°" + idJob3
            + " est attendu parmi les échecs", jobsFailure.contains(idJob3));

      assertJobFailureInfo(idJob1, "idJob1", 3,
            "exception n°3 levée par le job " + idJob1 + " ");
      assertJobFailureInfo(idJob3, "idJob3", 5,
            "exception n°5 levée par le job " + idJob3 + " ");

   }

   private void assertJobFailureInfo(UUID idJob, String identifiant,
         int expectedCount, String expectedMessage) {

      JobFailureInfo failureInfo = jobFailureService.getJobsFailure()
            .get(idJob);

      Assert.assertEquals("le traitement " + identifiant + " n°" + idJob
            + " n'a pas le nombre d'échecs attendus", expectedCount,
            failureInfo.getCountAnomalie());

      Assert.assertEquals("le traitement " + identifiant + " n°" + idJob
            + " n'a pas l'echec attendu", expectedMessage, failureInfo
            .getLastAnomalie().getMessage());
   }

   private void ajouterEchec(UUID idJob, int tentatives) {

      Exception exception = new NestableException("exception n°" + tentatives
            + " levée par le job " + idJob + " ");

      jobFailureService.ajouterEchec(idJob, exception);
   }

}
