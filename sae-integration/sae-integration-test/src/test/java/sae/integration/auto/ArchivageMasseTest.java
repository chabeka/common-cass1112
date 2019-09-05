package sae.integration.auto;

import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapHelper;
import sae.integration.util.ArchivageSommaireBuilder;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test les traitements d'archivage de masse
 */
public class ArchivageMasseTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageMasseTest.class);


   @Test
   /**
    * On crée un sommaire avec 2 documents, lance l'archivage de masse, et on vérifie que les deux
    * documents ont été correctement archivés
    * 
    * @throws Exception
    */
   public void archivageOK() throws Exception {
      final Environment environnement = Environments.GNT_PIC;
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(environnement.getUrl());

      // Création du sommaire, avec deux documents
      final ArchivageSommaireBuilder builder = new ArchivageSommaireBuilder();
      final ListeMetadonneeType metas1 = RandomData.getRandomMetadatasWithGedId();
      final String docId1 = SoapHelper.getMetaValue(metas1, "IdGed");
      final String filePath1 = TestData.addPdfFileMeta(metas1);
      builder.addDocument(filePath1, metas1);
      final ListeMetadonneeType metas2 = RandomData.getRandomMetadatasWithGedId();
      final String docId2 = SoapHelper.getMetaValue(metas2, "IdGed");
      final String filePath2 = TestData.addTiffFileMeta(metas2);
      builder.addDocument(filePath2, metas2);
      final String sommaireContent = builder.build();

      try (final JobManager job = new JobManager(environnement)) {
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.launchArchivageMasse(sommaireContent, builder.getFilePaths(), builder.getFileTargetNames());
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         final String resultatsXML = job.getResultatsXML();
         LOGGER.debug("Contenu du fichier resultat.xml :\r\n {}\r\n", resultatsXML);

         // On vérifie que les deux documents ont bien été archivés, avec les bonnes métadonnées
         LOGGER.info("Validation du document {}", docId1);
         ArchivageValidationUtils.validateDocument(service, docId1, metas1);
         LOGGER.info("Validation du document {}", docId2);
         ArchivageValidationUtils.validateDocument(service, docId2, metas2);

         // TODO : vérification du contenu du fichier resultats.xml

      }
      finally {
         LOGGER.info("Suppression des documents");
         CleanHelper.deleteOneDocument(service, docId1);
         CleanHelper.deleteOneDocument(service, docId2);
      }
   }


}
