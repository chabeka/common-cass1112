package sae.integration.auto.transfert.masse;

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
import sae.integration.util.TransfertSommaireBuilder;
import sae.integration.util.XMLHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.xml.modele.ResultatsType;

/**
 * Test les traitements de transfert de masse
 */
public class TransfertMasseOKTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertMasseOKTest.class);

   @Test
   /**
    * On archive 3 documents en GNT. On crée un sommaire avec 2 documents à
    * transférer, et un à supprimer. On vérifie que les 2 documents arrivent bien
    * en GNS, et que le document supprimé est bien supprimé
    * 
    * @throws Exception
    */
   public void transfertOK() throws Exception {
      final Environment environnementGNT = Environments.GNT_INT_PAJE;
      final Environment environnementGNS = Environments.GNS_INT_PAJE;
      final SaeServicePortType gntService = SaeServiceStubFactory
            .getServiceForDevToutesActions(environnementGNT.getUrl());
      final SaeServicePortType gnsService = SaeServiceStubFactory
            .getServiceForDevToutesActions(environnementGNS.getUrl());

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

      // Création du sommaire
      final TransfertSommaireBuilder builder = new TransfertSommaireBuilder();
      // Pour le doc 0 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas0 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas0, "Siren", "123456789");
      builder.addTransfert(uuids.get(0), modifiedMetas0);

      // Pour le doc 1 : on demande sa mise à la corbeille
      builder.addDeletion(uuids.get(1));

      // Pour le doc 2 : on demande son transfert sans modification de méta
      final ListeMetadonneeType modifiedMetas2 = new ListeMetadonneeType();
      builder.addTransfert(uuids.get(2), modifiedMetas2);
      final String sommaireContent = builder.build();

      try (final JobManager job = new JobManager(environnementGNT)) {
         // Préparation et lancement du job
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.launchTransfertMasse(sommaireContent);

         // Récupération du log du traitement et du resultats.xml pour debug
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         final String resultatsXML = job.getResultatsXML();
         LOGGER.debug("Contenu du fichier resultats.xml :\r\n {}\r\n", resultatsXML);

         // On vérifie que le document 0 a bien été transféré, avec les bonnes
         // métadonnées
         LOGGER.info("Validation du document {}", uuids.get(0));
         final ListeMetadonneeType finalMetas0 = metasList.get(0);
         SoapBuilder.setMetaValue(finalMetas0, "Siren", "123456789");
         ArchivageValidationUtils.validateDocument(gnsService, uuids.get(0), finalMetas0);
         LOGGER.info("Test d'existence en GNT du document {}", uuids.get(0));
         boolean existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(0));
         Assert.assertEquals(false, existsInGNT);

         // On vérifie que le document 1 a bien été supprimé
         LOGGER.info("Test d'existence en GNT et GNS du document {}", uuids.get(1));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(1));
         Assert.assertEquals(false, existsInGNT);
         final boolean existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(1));
         Assert.assertEquals(false, existsInGNS);

         // On vérifie que le document 2 a bien été transféré, avec les bonnes
         // métadonnées
         LOGGER.info("Validation du document {}", uuids.get(2));
         ArchivageValidationUtils.validateDocument(gnsService, uuids.get(2), metasList.get(2));
         LOGGER.info("Test d'existence en GNT du document {}", uuids.get(2));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(2));
         Assert.assertEquals(false, existsInGNT);

         // Vérification du contenu du fichier resultats.xml
         final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);
         Assert.assertEquals(3, (int) resultat.getInitialDocumentsCount());
         Assert.assertEquals(3, (int) resultat.getIntegratedDocumentsCount());

      } finally {
         LOGGER.info("Suppression des documents");
         for (int i = 0; i < 3; i++) {
            CleanHelper.deleteOneDocument(gntService, uuids.get(i));
            CleanHelper.deleteOneDocument(gnsService, uuids.get(i));
         }
      }
   }

}
