package fr.urssaf.image.sae.ordonnanceur.support.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.Assert;

import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;

@SuppressWarnings("PMD.MethodNamingConventions")
public class CaptureMasseLauncherSupportImplTest {

   @Test
   public void constructeur_failure_saeconfig_notfound() {

      Resource saeConfigResource = new ClassPathResource(
            "/sae-config.properties");

      try {
         new CaptureMasseLauncherSupportImpl("executable", saeConfigResource);

         Assert
               .fail("une exception de type OrdonnanceurRuntimeException doit être levée");

      } catch (OrdonnanceurRuntimeException e) {

         Assert.assertEquals("le message de l'exception est inattendu",
               "Erreur lors de la lecture du fichier de configuration du SAE.",
               e.getMessage());
      }
   }

   @Test
   public void createCommand_success() throws IOException {

      Resource saeConfigResource = new FileSystemResource(
            "src/test/resources/config/sae-config-test.properties");

      Properties saeConfiguration = new Properties();
      saeConfiguration.load(saeConfigResource.getInputStream());

      String executable = saeConfiguration
            .getProperty("sae.archivagemasse.executable");

      CaptureMasseLauncherSupportImpl launcher = new CaptureMasseLauncherSupportImpl(
            executable, saeConfigResource);

      String idTraitement = "1847fbd3-3eee-4e77-9c05-c78a62845c7e";

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      parameters.put("capture.masse.idtraitement", new JobParameter(
            idTraitement));
      JobParameters jobParameters = new JobParameters(parameters);

      long idJob = 3;

      JobInstance captureMasse = new JobInstance(idJob, jobParameters,
            "jobTest");
      String commande = launcher.createCommand(captureMasse);

      StrBuilder expectedCommand = new StrBuilder();

      expectedCommand.append("java -jar -Xms500m -Xmx500m");
      expectedCommand.append(" -DLOGS_UUID=");
      expectedCommand.append(idTraitement);
      expectedCommand.append(" -Dlogback.configurationFile=");
      expectedCommand.append("logback-sae-services-executable.xml");
      expectedCommand.append(" -Dfile.encoding=UTF-8");
      expectedCommand.append(" sae-services-executable.jar");
      expectedCommand.append(" captureMasse");
      expectedCommand.append(" 3");
      expectedCommand.append(" "
            + saeConfigResource.getFile().getAbsolutePath());
      expectedCommand.append(" " + idTraitement);

      Assert.assertEquals("l'exécutable du traitement de masse est inattendu",
            expectedCommand.toString(), commande);
   }

   @Test
   public void createCommand_failure_empty_idtraitement() {

      Resource saeConfigResource = new FileSystemResource(
            "src/test/resources/config/sae-config-test.properties");

      CaptureMasseLauncherSupportImpl launcher = new CaptureMasseLauncherSupportImpl(
            "executable", saeConfigResource);

      Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
      JobParameters jobParameters = new JobParameters(parameters);

      long idJob = 3;

      JobInstance captureMasse = new JobInstance(idJob, jobParameters,
            "jobTest");

      try {

         launcher.createCommand(captureMasse);

         Assert
               .fail("Une exception de type IllegalArgumentException doit être levée");

      } catch (IllegalArgumentException e) {

         Assert
               .assertEquals(
                     "Le message de l'exception est inattendu",
                     "Le paramètre 'capture.masse.idtraitement' du traitement de capture en masse doit être renseigné",
                     e.getMessage());
      }

   }
}
