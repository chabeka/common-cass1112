package fr.urssaf.image.sae.ordonnanceur.support.impl;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang.text.StrBuilder;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import fr.urssaf.image.sae.ordonnanceur.exception.OrdonnanceurRuntimeException;
import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import junit.framework.Assert;

@SuppressWarnings("PMD.MethodNamingConventions")
public class TraitementMasseLauncherSupportImplTest {

   private static final String MEMORY_VALUE = "500m";

   @Test
   public void constructeur_failure_saeconfig_notfound() {

      Resource saeConfigResource = new ClassPathResource(
            "/sae-config.properties");

      try {
         new TraitementMasseLauncherSupportImpl("executable", MEMORY_VALUE,
               MEMORY_VALUE, saeConfigResource);

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
            .getProperty("sae.traitementmasse.executable");

      TraitementMasseLauncherSupportImpl launcher = new TraitementMasseLauncherSupportImpl(
            executable, MEMORY_VALUE, MEMORY_VALUE, saeConfigResource);

      String parameters = "ecde://ecde.cer69.recouv/sommaire.xml";
      UUID idJob = UUID.randomUUID();

      JobQueue captureMasse = new JobQueue();

      captureMasse.setType("jobTest");
      captureMasse.setIdJob(idJob);
      captureMasse.setParameters(parameters);

      String commande = launcher.createCommand(captureMasse);

      StrBuilder expectedCommand = new StrBuilder();

      expectedCommand.append("java -jar @GED.CM_JAVA_OPTS@");
      expectedCommand.append(" -DLOGS_UUID=");
      expectedCommand.append(idJob);
      expectedCommand.append(" -Dlogback.configurationFile=");
      expectedCommand.append("logback-sae-services-executable.xml");
      expectedCommand.append(" -Dfile.encoding=UTF-8");
      expectedCommand.append(" sae-services-executable.jar");
      expectedCommand.append(" traitementMasse");
      expectedCommand.append(" " + idJob);
      expectedCommand.append(" "
            + saeConfigResource.getFile().getAbsolutePath());

      Assert.assertEquals("l'exécutable du traitement de masse est inattendu",
            expectedCommand.toString(), commande);
   }

}
