package fr.urssaf.image.sae.ordonnanceur.support;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-ordonnanceur-service-test.xml",
      "/applicationContext-sae-ordonnanceur-mock-test.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class CaptureMasseSupportTest {

   @Autowired
   private CaptureMasseSupport captureMasseSupport;

   private static final String CAPTURE_MASSE_JN = "capture_masse";

   @Test
   public void filtrerCaptureMasseLocal() {

      List<SimpleJobRequest> jobs = new ArrayList<SimpleJobRequest>();

      // traitement de capture en masse local
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer69.recouv/sommaire.xml"));
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer34.recouv/sommaire.xml"));
      // traitement de capture en masse non local
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer44.recouv/sommaire.xml"));
      // traitement de capture en masse avec une URL ECDE non configurée
      jobs.add(createJob(CAPTURE_MASSE_JN,
            "ecde://ecde.cer78.recouv/sommaire.xml"));
      // autre traitement de masse
      jobs.add(createJob("OTHER_JN", "OTHER_PARAMETERS"));
      // traitement de capture en masse sans paramètre pour URL ECDE
      jobs.add(createJob(CAPTURE_MASSE_JN, "OTHER_PARAMETERS"));
      // traitement de capture en masse avec paramètre pour URL ECDE erroné
      jobs.add(createJob(CAPTURE_MASSE_JN, "ecde://azaz^^/sommaire.xml"));

      List<SimpleJobRequest> traitements = captureMasseSupport
            .filtrerCaptureMasseLocal(jobs);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 2,
            traitements.size());

      Assert.assertEquals("le traitement est inattendu", jobs.get(0),
            traitements.get(0));
      Assert.assertEquals("le traitement de capture est inattendu",
            jobs.get(1), traitements.get(1));
   }

   @Test
   public void filtrerJobExecutionLocal() {

      List<SimpleJobRequest> jobs = new ArrayList<SimpleJobRequest>();

      // traitement de capture en masse
      jobs.add(createJob(CAPTURE_MASSE_JN));
      jobs.add(createJob(CAPTURE_MASSE_JN));

      // autre traitement de masse
      jobs.add(createJob("OTHER_JN1"));
      jobs.add(createJob("OTHER_JN2"));

      List<SimpleJobRequest> traitements = captureMasseSupport
            .filtrerCaptureMasse(jobs);

      Assert.assertEquals("le nombre de traitements filtrés est inattendu", 2,
            traitements.size());

      Assert.assertEquals("le job de capture en masse est inattendu", jobs
            .get(0), traitements.get(0));
      Assert.assertEquals("le job de capture en masse est inattendu", jobs
            .get(1), traitements.get(1));
   }

   private SimpleJobRequest createJob(String type, String parameters) {

      SimpleJobRequest job = createJob(type);
      job.setParameters(parameters);

      return job;
   }

   private SimpleJobRequest createJob(String type) {

      UUID idJob = UUID.randomUUID();

      SimpleJobRequest job = new JobRequest();

      job.setType(type);
      job.setIdJob(idJob);

      return job;
   }

}
