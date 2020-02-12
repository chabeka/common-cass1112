package sae.integration.auto.transfert.masse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

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

   // @Test
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

   @Test
   /**
    * Test de rejet d'un transfert lorsque la meta Montant2 contient une valeur non numérique
    * Pour cela nous archive 7 documents en GNT.
    * Puis on transfère ces documents en GNS en respectant les conditions suivantes:
    * Le premier et le troisième document sont bien transférés
    * Dans le second, on duplique la meta denomination pour faire echouer le transfert
    * Dans le quatrième on provoque une erreur sur la valeur de la meta montant2 en envoyant une chaine
    * 
    * @throws Exception
    */
   public void transfertWithMetaMontantStringKO() throws Exception {
      final Environment environnementGNT = Environments.GNT_INT_PAJE;
      final Environment environnementGNS = Environments.GNS_INT_PAJE;
      final SaeServicePortType gntService = SaeServiceStubFactory
            .getServiceForDevToutesActions(environnementGNT.getUrl());
      final SaeServicePortType gnsService = SaeServiceStubFactory
            .getServiceForDevToutesActions(environnementGNS.getUrl());

      // Archivage de 4 documents en GNT
      final List<ListeMetadonneeType> metasList = new ArrayList<>();
      final List<String> uuids = new ArrayList<>();
      for (int i = 0; i < 8; i++) {
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

      // Pour le doc 1 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas1 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas1, "Siren", "123456789");
      SoapBuilder.addMeta(modifiedMetas1, "Denomination", "PPPP");
      SoapBuilder.addMeta(modifiedMetas1, "Denomination", "AAAA");
      builder.addTransfert(uuids.get(1), modifiedMetas1);

      // Pour le doc 2 : on demande son transfert sans modification de méta
      final ListeMetadonneeType modifiedMetas2 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas2, "Siren", "123456789");
      builder.addTransfert(uuids.get(2), modifiedMetas2);

      // Pour le doc 3 : on demande son transfert
      final ListeMetadonneeType modifiedMetas3 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas3, "Siren", "111111111");
      SoapBuilder.addMeta(modifiedMetas3, "Montant2", "C");
      // SoapBuilder.addMeta(modifiedMetas3, "CodeRND", "");
      builder.addTransfert(uuids.get(3), modifiedMetas3);

      // Pour le doc 4 : on demande sa mise à la corbeille
      builder.addDeletion(uuids.get(4));

      // Pour le doc 5 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas5 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas5, "Siren", "123456789");
      builder.addTransfert(uuids.get(5), modifiedMetas5);
      SoapBuilder.addMeta(modifiedMetas5, "CodeRND", "");

      // Pour le doc 6 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas6 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas6, "Siren", "123456789");
      SoapBuilder.addMeta(modifiedMetas6, "Montant2", "VV");
      builder.addTransfert(uuids.get(6), modifiedMetas6);

      // Pour le doc 7 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas7 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas7, "Siren", "123456789");
      builder.addTransfert(uuids.get(7), modifiedMetas7);

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
         final ListeMetadonneeType finalMetas0 = metasList.get(0);
         LOGGER.info("Test d'existence en GNT du document {} index 0", uuids.get(0));
         boolean existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(0));
         Assert.assertEquals(false, existsInGNT);
         boolean existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(0));
         Assert.assertEquals(true, existsInGNS);

         // On vérifie que le document 1 n'est pas transféré
         LOGGER.info("Test d'existence en GNT et GNS du document {} index 1", uuids.get(1));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(1));
         Assert.assertEquals(true, existsInGNT);
         existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(1));
         Assert.assertEquals(false, existsInGNS);

         // On vérifie que le document 2 a bien été transféré, avec les bonnes
         // métadonnées
         LOGGER.info("Test d'existence en GNT du document {} index 2", uuids.get(2));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(2));
         Assert.assertEquals(false, existsInGNT);
         existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(2));
         Assert.assertEquals(true, existsInGNS);

         // On vérifie que le document 3 n'a pas été transféré
         LOGGER.info("Test d'existence en GNT et GNS du document {} index 3", uuids.get(3));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(3));
         Assert.assertEquals(true, existsInGNT);
         existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(3));
         Assert.assertEquals(false, existsInGNS);

         // On vérifie que le document 4 a bien été supprimé
         LOGGER.info("Test d'existence en GNT et GNS du document {} 4", uuids.get(4));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(4));
         Assert.assertEquals(false, existsInGNT);
         existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(4));
         Assert.assertEquals(false, existsInGNS);

         // On vérifie que le document 5 n'a pas été transféré
         LOGGER.info("Test d'existence en GNT du document {} 5", uuids.get(5));
         existsInGNT = ArchivageValidationUtils.docExists(gntService, uuids.get(5));
         Assert.assertEquals(true, existsInGNT);
         existsInGNS = ArchivageValidationUtils.docExists(gnsService, uuids.get(5));
         Assert.assertEquals(false, existsInGNS);

         // Vérification du contenu du fichier resultats.xml
         LOGGER.info("Validation du fichier résultat!");
         final ResultatsType resultat = XMLHelper.parseResultatsXML(resultatsXML);

         // Recuperation des erreurs du document 3
         LOGGER.info("Validation Des erreurs sur la métadonnée Montant2");
         final String montantErrorPartString = "mt2 criterion value is not a parsable";

         LOGGER.info("Nombre de documents non intégrés est de : {}", resultat.getNonIntegratedDocuments().getNonIntegratedDocument().size());

         // On recupère l'erreur sur le 2ème document en erreur
         assertNotEquals(resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(1).getErreurs().getErreur().size(), 0);
         final String erreurSurLeDoc2 = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(1).getErreurs().getErreur().get(0).getLibelle();
         assertThat(erreurSurLeDoc2, containsString(montantErrorPartString));

         // On recupère l'erreur sur le 4ème document en erreur
         assertNotEquals(resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(3).getErreurs().getErreur().size(), 0);
         final String erreurSurLeDoc4 = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(3).getErreurs().getErreur().get(0).getLibelle();
         assertThat(erreurSurLeDoc4, containsString(montantErrorPartString));

         // On recupère l'erreur sur le 6ème document en erreur
         assertNotEquals(resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(5).getErreurs().getErreur().size(), 0);
         final String erreurSurLeDoc6 = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(5).getErreurs().getErreur().get(0).getLibelle();
         assertThat(erreurSurLeDoc6, containsString(montantErrorPartString));

         LOGGER.info("Validation Du nombre de documents intégrés");
         Assert.assertEquals(8, (int) resultat.getInitialDocumentsCount());
         Assert.assertEquals(4, (int) resultat.getIntegratedDocumentsCount());
         LOGGER.info("Fin Validation du fichier résultat!");
      } finally {
         LOGGER.info("Suppression des documents");

         // Doc a supprimer en GNT
         CleanHelper.deleteOneDocument(gntService, uuids.get(3));
         CleanHelper.deleteOneDocument(gntService, uuids.get(5));
         CleanHelper.deleteOneDocument(gntService, uuids.get(6));

         // Doc a supprimer en GNS
         CleanHelper.deleteOneDocument(gnsService, uuids.get(0));
         CleanHelper.deleteOneDocument(gnsService, uuids.get(2));
         CleanHelper.deleteOneDocument(gnsService, uuids.get(7));
      }
   }

}
