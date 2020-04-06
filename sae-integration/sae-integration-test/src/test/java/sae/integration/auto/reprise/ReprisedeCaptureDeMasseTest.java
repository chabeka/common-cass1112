package sae.integration.auto.reprise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageSommaireBuilder;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapHelper;
import sae.integration.util.XMLHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.xml.modele.ResultatsType;

/**
 * Permet d'éffectuer une reprise de capture de masse sur 2 documents
 * On lance une capture masse en stoppant le tomcat du webservice DFCE
 * sur l'environnement courant d'exécution du test.
 * Tous les 2 documents tombent en erreur car DFCE inaccessible.
 * On redémarre tomcat avant le lancement de la reprise du job de capture
 * Après la reprise, les 2 documents sont insérés avec succès.
 * A savoir qu'il y a un temps d'attente
 */
public class ReprisedeCaptureDeMasseTest {

   private static Environment environment;

   private static SaeServicePortType serviceGNT;

   private static List<String> uuids;

   private JobManager jobCapture;

   /**
    * Nombre de documents initial dans la capture de masse
    */
   private final static int DOCUMENTS_COUNT = 2;

   private static final Logger LOGGER = LoggerFactory.getLogger(ReprisedeCaptureDeMasseTest.class);

   /**
    * Temps d'attente entre la fin de la capture masse en échec et la reprise
    * afin de nous permettre de redémarrer TOMCAT.
    */
   private static final int ATTENTE_POUR_REDEMARRAGE_TOMCAT = 20;

   boolean withRemoteDebug = true;

   @BeforeClass
   public static void setup() {
      environment = Environments.GNT_INT_PAJE;
      serviceGNT = SaeServiceStubFactory.getServiceForDevToutesActions(environment.getUrl());
      uuids = new ArrayList<>();
   }

   @Test
   /**
    * Permet de lancer une reprise de masse à la suite d'une capture de masse en état failed
    * Pour celà on arrête le serveur Tomcat sur l'environnement courant avant de lancer lancer le test
    * Puis redemarrer le tomcat avant que la reprise ne soit lancer
    * 
    * @throws Exception
    */
   public void repriseCaptureMasseTest() throws Exception {
      captureMasse();
      int i = ATTENTE_POUR_REDEMARRAGE_TOMCAT;
      // Une fois la capture de masse fini attendre 20 secondes le temps modifier le fichier sommaire
      // Afin de rectifier l'erreur sur la meta CodeRND
      LOGGER.info("Capture de masse terminée");
      LOGGER.info("En attente de redemarrage du serveur Tomcat...");
      while (i > 0) {
         LOGGER.info("Veuillez redémarrer Tomcat.... il vous reste {} secondes avant la reprise!", i);
         Thread.sleep(1000);
         i--;
      }
      LOGGER.info("Fin d'attente pour le redémarrage de tomcat");

      LOGGER.info("Exécution de la reprise de Capture masse");
      try {
         final JobManager job = new JobManager(environment);
         job.setRemoteDebug(withRemoteDebug);
         LOGGER.info("Lancement du Job de reprise {}", job.getJobId());
         LOGGER.info("Lancement de la reprise du Job reprise {}", jobCapture.getJobId());
         job.launchReprise(jobCapture.getJobId().toString());

         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", jobCapture.getJobLog());
         LOGGER.info("\r\n\r\n===== Fin log");

         final String resultatsXML = jobCapture.getResultatsXML();
         // Verification du resultats
         LOGGER.debug("Contenu du fichier resultat.xml :\r\n {}\r\n", resultatsXML);

         final ResultatsType resultatsType = XMLHelper.parseResultatsXML(resultatsXML);
         assertEquals(DOCUMENTS_COUNT, resultatsType.getIntegratedDocumentsCount().intValue());

      }
      catch (final Exception e) {
         e.printStackTrace();
         fail("Echec lors de la reprise de masse");
      }
      finally {
         LOGGER.info("Suppression de tous les documents");
         for (final String uuid : uuids) {
            CleanHelper.deleteOneDocument(serviceGNT, uuid);
         }
      }
   }

   /**
    * Permet de lancer une capture de masse sur 2 documents
    * Arrêter le serveur tomcat pour le service dfce sur l'enviromment courant
    * Le but étant de mettre cette capture de masse en échec
    */
   private void captureMasse() throws Exception {
      // Création du sommaire, avec deux documents
      final ArchivageSommaireBuilder builder = new ArchivageSommaireBuilder();
      final ListeMetadonneeType metas1 = RandomData.getRandomMetadatasWithGedId();
      final String docId1 = SoapHelper.getMetaValue(metas1, "IdGed");
      final String filePath1 = TestData.addPdfFileMeta(metas1);
      builder.addDocument(filePath1, metas1);
      uuids.add(docId1);

      final ListeMetadonneeType metas2 = RandomData.getRandomMetadatasWithGedId();
      final String docId2 = SoapHelper.getMetaValue(metas2, "IdGed");
      final String filePath2 = TestData.addTiffFileMeta(metas2);
      builder.addDocument(filePath2, metas2);
      uuids.add(docId2);

      final String sommaireContent = builder.build();

      jobCapture = new JobManager(environment);
      jobCapture.setRemoteDebug(withRemoteDebug);
      LOGGER.info("Lancement du job {}", jobCapture.getJobId());
      LOGGER.info("Veuillez arreter les serveurs tomcat avant le lancement manuel"
            + "de la capture de masse du job {} sur l'environnement {}"
            + "afin de mettre en echec la capture pour une eventuelle reprise",
            new Object[] {jobCapture.getJobId(), environment.getEnvCode()});
      jobCapture.launchArchivageMasse(sommaireContent, builder.getFilePaths(), builder.getFileTargetNames());

      // Affichage des logs
      final String log = jobCapture.getJobLog();
      LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);

      // Verification du resultat
      final String resultatsXML = jobCapture.getResultatsXML();
      LOGGER.debug("Contenu du fichier resultat.xml :\r\n {}\r\n", resultatsXML);
      final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);
      assertEquals(DOCUMENTS_COUNT, resultat.getInitialDocumentsCount().intValue());
      // On vérifie qu'aucun document n'a été intégré car nous avons stoppé Tomcat
      assertEquals(0, resultat.getIntegratedDocumentsCount().intValue());
      System.out.println(uuids);
   }

}
