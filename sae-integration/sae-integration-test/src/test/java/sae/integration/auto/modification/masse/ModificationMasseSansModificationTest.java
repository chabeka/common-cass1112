package sae.integration.auto.modification.masse;

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
import sae.integration.util.ModificationSommaireBuilder;
import sae.integration.util.XMLHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.xml.modele.ResultatsType;

/**
 * Test un traitement de modification de masse qui ne modifie rien
 */
public class ModificationMasseSansModificationTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ModificationMasseSansModificationTest.class);


   @Test
   /**
    * On archive 3 documents en GNT.
    * On crée un sommaire avec 3 documents à modifier, mais sans méta à modifié
    * On vérifie que les 3 documents ne sont pas modifiés
    * 
    * @throws Exception
    */
   public void modificationSansModification() throws Exception {
      final Environment environnementGNT = Environments.GNT_PIC;
      final SaeServicePortType gntService = SaeServiceStubFactory.getServiceForDevToutesActions(environnementGNT.getUrl());

      // Archivage de 3 documents en GNT
      final List<ListeMetadonneeType> metasList = new ArrayList<>();
      final List<String> uuids = new ArrayList<>();
      for (int i = 0; i < 3; i++) {
         final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
         final ListeMetadonneeType metas = RandomData.getRandomMetadatas();
         request.setMetadonnees(metas);
         request.setDataFile(TestData.getTxtFile(metas));
         metasList.add(metas);
         // Lancement de l'archivage
         uuids.add(ArchivageUtils.sendArchivageUnitaire(gntService, request));
      }

      // Création du sommaire de modification de masse
      final ModificationSommaireBuilder builder = new ModificationSommaireBuilder();
      for (int i = 0; i < 3; i++) {
         final ListeMetadonneeType modifiedMetas = new ListeMetadonneeType();
         builder.addDocument(uuids.get(i), modifiedMetas);
      }
      final String sommaireContent = builder.build();

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

         // On vérifie que les documents n'ont pas été modifiés
         for (int i = 0; i < 3; i++) {
            LOGGER.info("Validation du document {}", uuids.get(i));
            final ListeMetadonneeType finalMetas = metasList.get(i);
            ArchivageValidationUtils.validateDocument(gntService, uuids.get(i), finalMetas);
         }

         // Vérification du contenu du fichier resultats.xml
         final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);
         Assert.assertEquals(3, (int) resultat.getInitialDocumentsCount());
         Assert.assertEquals(3, (int) resultat.getIntegratedDocumentsCount());

      }
      finally {
         LOGGER.info("Suppression des documents");
         for (int i = 0; i < 3; i++) {
            CleanHelper.deleteOneDocument(gntService, uuids.get(i));
         }
      }
   }


}
