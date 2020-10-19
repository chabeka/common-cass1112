/**
 *  TODO (AC75095028) Description du fichier
 */
package sae.integration.manual;

import static com.rainerhahnekamp.sneakythrow.Sneaky.sneak;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
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
import sae.integration.util.ArchivageUtils;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.ModificationUtils;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ConsultationMTOMRequestType;
import sae.integration.webservice.modele.ConsultationMTOMResponseType;
import sae.integration.webservice.modele.ListeMetadonneeCodeType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;
import sae.integration.webservice.modele.RechercheRequestType;
import sae.integration.webservice.modele.RechercheResponseType;
import sae.integration.webservice.modele.ResultatRechercheType;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.SuppressionRequestType;

/**
 * TODO (AC75095028) Description du type
 *
 */
public class BoucleAppelWebServicesMigrationDonnees {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageUnitaireEnBoucleTest.class);

   private static SaeServicePortType service;

   private static Environment environment;

   List<String> listUUIDRetour = new ArrayList<>();

   List<String> listUUIDSupprimes = new ArrayList<>();

   private static Random randomGenerator;

   @BeforeClass
   public static void setup() {
      environment = Environments.GNT_INT_PAJE;
      service = SaeServiceStubFactory.getServiceForDevToutesActions(environment.getUrl());
      randomGenerator = new Random();
   }

   @Test
   public void datasetCreationTest() throws InterruptedException {

      final int iterationsCount = 100;

      // ArchivageUnitaire

      for (int i = 0; i < iterationsCount; i++) {
         final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
         request.setMetadonnees(RandomData.getRandomMetadatas());
         request.setDataFile(TestData.getTxtFile(request.getMetadonnees()));
         final String uuid = ArchivageUtils.sendArchivageUnitaire(service, request);
         LOGGER.info("UUID : {}", uuid);
         Assert.assertFalse("L'appel du webservice doit aboutir", uuid.isEmpty());

         // ajout dans la liste
         listUUIDRetour.add(uuid);

         // Pause de quelques seconde
         java.util.concurrent.TimeUnit.SECONDS.sleep(5);

      }

      // consultation
      for (int i = 0; i < 100; i++) {

         consultationDocExistantTest();

         // Pause de quelques seconde
         java.util.concurrent.TimeUnit.SECONDS.sleep(5);
      }


      // Modification

      for (int i = 0; i < 100; i++) {

         modificationDocExistantTest();

         // Pause de quelques seconde
         java.util.concurrent.TimeUnit.SECONDS.sleep(5);
      }

      // RECHERCHE

      rechercheTest();
      // Pause de quelques seconde
      java.util.concurrent.TimeUnit.SECONDS.sleep(5);


      // Suppression
      for (int i = 0; i < 5; i++) {
         suppressionTest();
         java.util.concurrent.TimeUnit.SECONDS.sleep(3);
      }

      // TRANSFERT

   }

   @Test
   /**
    * ARCHIVAGE DE MASSE
    * On crée un sommaire avec 2 documents, lance l'archivage de masse, et on vérifie que les deux
    * documents ont été correctement archivés
    * 
    * @throws Exception
    */
   public void archivageOK() throws Exception {

      final Environment environnement = Environments.GNT_INT_PAJE;
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(environnement.getUrl());

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

   public String consultationDocExistantTest() {

      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_PAJE.getUrl());

      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      final String docId = getAnyUUID();
      request.setIdArchive(docId);

      final ConsultationMTOMResponseType response = service.consultationMTOM(request);
      Assert.assertNotNull("L'appel du webservice doit aboutir", response);
      Assert.assertNotNull("Le contenu ne doit pas être null", response.getContenu());
      final boolean isContenu = sneak(() -> !IOUtils.isEmpty(response.getContenu().getInputStream()));
      Assert.assertTrue("L'appel du webservice doit aboutir", isContenu);

      return docId;
   }

   /**
    * MODIFICATION
    */
   public void modificationDocExistantTest() {

      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_PAJE.getUrl());

      final String uuid = getAnyUUID();
      // Faire une modification sur le doc uuid
      ModificationUtils.sendModification(service, uuid, "CodeOrganismeProprietaire", "555");

      // on consult pour voir que la modification à bien eu lieu
      final ConsultationMTOMRequestType requestConsult = new ConsultationMTOMRequestType();
      requestConsult.setIdArchive(uuid);

      final ConsultationMTOMResponseType responseConsult = service.consultationMTOM(requestConsult);
      Assert.assertNotNull("L'appel du webservice doit aboutir", responseConsult);
      Assert.assertNotNull("La liste des metadonnées ne doit pas être null", responseConsult.getMetadonnees());

      boolean isCodeModified = false;
      for (final MetadonneeType meta : responseConsult.getMetadonnees().getMetadonnee()) {
         if ("CodeOrganismeProprietaire".equals(meta.getCode())) {
            final String valeur = meta.getValeur();
            Assert.assertNotNull("L'appel du webservice doit aboutir", valeur);
            Assert.assertEquals("La valeur de retour doit être 555", valeur, "555");
            isCodeModified = true;
         }
      }

      Assert.assertTrue("Le code organisme doit être modifié", isCodeModified);
   }

   /**
    * RECHERCHE
    */
   public void rechercheTest() {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_PAJE.getUrl());

      final RechercheRequestType request = new RechercheRequestType();
      final ListeMetadonneeCodeType metasToGet = new ListeMetadonneeCodeType();
      metasToGet.getMetadonneeCode().add("Titre");
      request.setMetadonnees(metasToGet);
      request.setRequete("Titre:TitreTestMig*");

      final RechercheResponseType response = service.recherche(request);
      final List<ResultatRechercheType> resultats = response.getResultats().getResultat();
      //
      Assert.assertNotNull("L'appel du webservice doit aboutir", resultats);
      Assert.assertTrue("La liste des resultats ne doit pas être vide", resultats.size() > 0);

   }

   /**
    * SUPPRESSION
    */
   public void suppressionTest() {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_PAJE.getUrl());

      // on prende un uuid au hasard dans la liste
      final String uuid = getAnyUUID();
      listUUIDSupprimes.add(uuid);

      final SuppressionRequestType request = new SuppressionRequestType();
      request.setUuid(uuid);

      // on fait une consultation sur l'uuid supprimes pour s'assurer qu'il a été bien supprimé
      final ConsultationMTOMRequestType requestConsult = new ConsultationMTOMRequestType();
      requestConsult.setIdArchive(uuid);

      final ConsultationMTOMResponseType responseConsult = service.consultationMTOM(requestConsult);
      Assert.assertNotNull("L'appel du webservice suppression doit aboutir", responseConsult);
      Assert.assertTrue("Le liste des métadonnées doit être vide", responseConsult.getMetadonnees().getMetadonnee().size() == 0);

   }
   /**
    * prendre un uuid au hasard
    * 
    * @return
    */
   public String getAnyUUID() {

      final int index = randomGenerator.nextInt(listUUIDRetour.size());
      final String item = listUUIDRetour.get(index);
      return item;
   }
}
