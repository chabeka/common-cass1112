package sae.integration.auto.modification.masse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.XMLHelper;
import sae.integration.xml.modele.ResultatsType;

/**
 * Test un traitement de modification de masse avec un fichier sommaire.xml vide
 */
public class ModificationMasseFichierSommaireVideTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ModificationMasseFichierSommaireVideTest.class);


   @Test
   /**
    * On crée un sommaire vide
    * On vérifie qu'on a bien l'erreur attendue dans le fichier resultats.xml
    * 
    * @throws Exception
    */
   public void fichierSommaireVide() throws Exception {
      final Environment environnementGNT = Environments.GNT_PIC;

      // Fichier sommaire vide
      final String sommaireContent = "";

      try (final JobManager job = new JobManager(environnementGNT)) {
         // Préparation et lancement du job
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.launchModificationMasse(sommaireContent);

         // Récupération du log du traitement et du resultats.xml pour debug
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         final String resultatsXML = job.getResultatsXML();
         LOGGER.debug("Contenu du fichier resultats.xml :\r\n {}\r\n", resultatsXML);

         // Vérification du contenu du fichier resultats.xml
         final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);
         final String libErreur = resultat.getErreurBloquanteTraitement().getLibelle();
         assertThat(libErreur, containsString("Le fichier sommaire n'est pas valide"));
      }
   }


}
