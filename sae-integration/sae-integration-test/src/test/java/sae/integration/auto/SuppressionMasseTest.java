package sae.integration.auto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapBuilder;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test les traitements de suppression de masse
 */
public class SuppressionMasseTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionMasseTest.class);


   @Test
   /**
    * Suppression de masse
    */
   public void suppressionOK() throws Exception {
      final Environment environnementGNT = Environments.FRONTAL_DEV;
      final SaeServicePortType gntService = SaeServiceStubFactory.getServiceForDevToutesActions(environnementGNT.getUrl());

      // Archivage de 3 documents en GNT
      final List<ListeMetadonneeType> metasList = new ArrayList<>();
      final List<String> uuids = new ArrayList<>();
      final String randomString = UUID.randomUUID().toString();
      for (int i = 0; i < 3; i++) {
         final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
         final ListeMetadonneeType metas = RandomData.getRandomMetadatas();
         SoapBuilder.setMetaValue(metas, "Denomination", randomString);
         request.setMetadonnees(metas);
         request.setDataFile(TestData.getTxtFile(metas));
         metasList.add(metas);
         // Lancement de l'archivage
         uuids.add(ArchivageUtils.sendArchivageUnitaire(gntService, request));
      }

      try (final JobManager job = new JobManager(environnementGNT)) {
         // Préparation et lancement du job
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.launchSuppressionMasse("Denomination:" + randomString);

         // Récupération du log du traitement et du resultats.xml pour debug
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);

         // On vérifie que les documents ont bien été supprimés
         for (int i = 0; i < 3; i++) {
            LOGGER.info("Test d'existence en GNT du document {}", uuids.get(i));
            final boolean existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(i));
            Assert.assertEquals(false, existsInGNT);
         }
      }
      finally {
         LOGGER.info("Nettoyage des documents");
         for (int i = 0; i < 3; i++) {
            CleanHelper.deleteOneDocument(gntService, uuids.get(i));
         }
      }

   }


}
