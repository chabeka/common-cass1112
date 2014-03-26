/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.controle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.util.test.EcdeTestSommaire;
import fr.urssaf.image.sae.ecde.util.test.EcdeTestTools;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = { "/applicationContext-sae-services-capturemasse-test.xml" })
public class ControleStepTest {

   @Autowired
   private JobLauncherTestUtils launcher;

   @Autowired
   private EcdeTestTools ecdeTestTools;

   private EcdeTestSommaire ecdeTestSommaire;

   @Before
   public final void init() {
      ecdeTestSommaire = ecdeTestTools.buildEcdeTestSommaire();
   }

   @After
   public final void end() {
      try {
         ecdeTestTools.cleanEcdeTestSommaire(ecdeTestSommaire);
      } catch (IOException e) {
         // rien a faire
      }
   }

   @Test
   @Ignore
   public final void testLancement() throws Exception {

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put(Constantes.SOMMAIRE, new JobParameter(ecdeTestSommaire
            .getUrlEcde().toString()));
      parameters.put(Constantes.ID_TRAITEMENT, new JobParameter(UUID
            .randomUUID().toString()));

      JobParameters jobParameters = new JobParameters(parameters);

      JobExecution execution = launcher.launchJob(jobParameters);

      execution.getExecutionContext();
   }

}
