package sae.integration.auto.transfert.masse;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

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
import sae.integration.xml.modele.ErreurType;
import sae.integration.xml.modele.ResultatsType;

/**
 * Test les traitements de transfert de masse avec erreur sur la metadonnée Montant 2.
 * Montant2 avec une valeur non numérique
 */
public class TransfertMasseMontantKOTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertMasseMontantKOTest.class);

   @Test
   /**
    * Test de rejet d'un transfert lorsque la meta Montant2 contient une valeur non numérique
    * Pour cela nous archive 8 documents en GNT.
    * Puis on transfère ces documents en GNS en respectant les conditions suivantes:
    * Le premier et le troisième document sont bien transférés
    * Dans le second, on duplique la meta denomination pour faire échouer le transfert
    * Dans le quatrième et le 7eme, on provoque une erreur sur la valeur de la meta montant2 en envoyant une chaine
    * 
    * @throws Exception
    */
   public void transfertWithMetaMontantStringKO() throws Exception {
      final Environment environnementGNT = Environments.LOCAL_BATCH;
      final Environment environnementGNS = Environments.GNS_INT_PAJE;
      final boolean withRemoteDebug = false;
      final SaeServicePortType gntService = SaeServiceStubFactory
            .getServiceForDevToutesActions(environnementGNT.getUrl());
      final SaeServicePortType gnsService = SaeServiceStubFactory
            .getServiceForDevToutesActions(environnementGNS.getUrl());

      // Archivage de 8 documents en GNT
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

      int errorIndex = 0;

      // Création du sommaire
      final TransfertSommaireBuilder builder = new TransfertSommaireBuilder();
      // Pour le doc 0 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas0 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas0, "Siren", "123456789");
      builder.addTransfert(uuids.get(0), modifiedMetas0);

      // Pour le doc 1 : on demande son transfert. Échec attendu, car dénomination en double
      final ListeMetadonneeType modifiedMetas1 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas1, "Siren", "123456789");
      SoapBuilder.addMeta(modifiedMetas1, "Denomination", "PPPP");
      SoapBuilder.addMeta(modifiedMetas1, "Denomination", "AAAA");
      builder.addTransfert(uuids.get(1), modifiedMetas1);
      final int doublonErrorIndex = errorIndex++;
      System.out.println(doublonErrorIndex);

      // Pour le doc 2 : on demande son transfert sans modification de méta
      final ListeMetadonneeType modifiedMetas2 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas2, "Siren", "123456789");
      builder.addTransfert(uuids.get(2), modifiedMetas2);

      // Pour le doc 3 : on demande son transfert. Échec attendu, car Montant2 en valeur non numérique
      final ListeMetadonneeType modifiedMetas3 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas3, "Siren", "111111111");
      SoapBuilder.addMeta(modifiedMetas3, "Montant2", "C");
      builder.addTransfert(uuids.get(3), modifiedMetas3);
      final int montantErrorIndex1 = errorIndex++;

      // Pour le doc 4 : on demande sa mise à la corbeille
      builder.addDeletion(uuids.get(4));

      // Pour le doc 5 : on demande son transfert. Échec attendu, car Code RND vide
      final ListeMetadonneeType modifiedMetas5 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas5, "Siren", "123456789");
      builder.addTransfert(uuids.get(5), modifiedMetas5);
      SoapBuilder.addMeta(modifiedMetas5, "CodeRND", "");
      final int codeRNDErrorIndex = errorIndex++;

      // Pour le doc 6 : on demande son transfert. Échec attendu, car Montant2 en valeur non numérique
      final ListeMetadonneeType modifiedMetas6 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas6, "Siren", "123456789");
      SoapBuilder.addMeta(modifiedMetas6, "Montant2", "VV");
      builder.addTransfert(uuids.get(6), modifiedMetas6);
      final int montantErrorIndex2 = errorIndex++;

      // Pour le doc 7 : on demande son transfert avec modification du siren
      final ListeMetadonneeType modifiedMetas7 = new ListeMetadonneeType();
      SoapBuilder.addMeta(modifiedMetas7, "Siren", "123456789");
      builder.addTransfert(uuids.get(7), modifiedMetas7);

      final String sommaireContent = builder.build();

      try (final JobManager job = new JobManager(environnementGNT)) {
         // Préparation et lancement du job
         final UUID jobId = job.getJobId();
         LOGGER.info("Lancement du job {}", jobId);
         job.setRemoteDebug(withRemoteDebug);
         job.launchTransfertMasse(sommaireContent);

         // Récupération du log du traitement et du resultats.xml pour debug
         final String log = job.getJobLog();
         LOGGER.debug("Log du traitement :\r\n\r\n{}\r\n", log);
         final String resultatsXML = job.getResultatsXML();
         LOGGER.debug("Contenu du fichier resultats.xml :\r\n {}\r\n", resultatsXML);

         // On vérifie que le document 0 a bien été transféré, avec les bonnes
         // métadonnées
         LOGGER.info("Test d'existence en GNT et GNS du document {} index 0", uuids.get(0));
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

         // On vérifie que le document 2 a bien été transféré, avec les bonnes métadonnées
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

         // Récupération des erreurs du document 3
         LOGGER.info("Validation Des erreurs sur la métadonnée Montant2");
         final String montantErrorPartString = "mt2 criterion value is not a parsable";
         final String rndErrorPartString = "La ou les métadonnées suivantes, obligatoires lors de l'archivage, ne sont pas renseignées : CodeRND";
         final String doublonMetaError = "La ou les métadonnées suivantes sont renseignées plusieurs fois : Denomination";

         LOGGER.info("Nombre de documents non intégrés est de : {}", resultat.getNonIntegratedDocuments().getNonIntegratedDocument().size());

         // On récupère l'erreur sur le 1er document en erreur
         final List<ErreurType> doc1Errors = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(doublonErrorIndex).getErreurs().getErreur();
         assertNotEquals(doc1Errors.size(), 0);
         final String erreurSurLeDoc1 = doc1Errors.get(0).getLibelle();
         assertThat(erreurSurLeDoc1, containsString(doublonMetaError));

         // On récupère l'erreur sur le 2ème document en erreur
         final List<ErreurType> doc2Errors = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(montantErrorIndex1).getErreurs().getErreur();
         assertNotEquals(doc2Errors.size(), 0);
         final String erreurSurLeDoc2 = doc2Errors.get(0).getLibelle();
         assertThat(erreurSurLeDoc2, containsString(montantErrorPartString));

         // On récupère l'erreur sur le 3ème document en erreur
         final List<ErreurType> doc3Errors = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(codeRNDErrorIndex).getErreurs().getErreur();
         assertNotEquals(doc3Errors.size(), 0);
         final String erreurSurLeDoc3 = doc3Errors.get(0).getLibelle();
         assertThat(erreurSurLeDoc3, containsString(rndErrorPartString));

         // On récupère l'erreur sur le 4ème document en erreur
         final List<ErreurType> doc4Errors = resultat.getNonIntegratedDocuments().getNonIntegratedDocument().get(montantErrorIndex2).getErreurs().getErreur();
         assertNotEquals(doc4Errors.size(), 0);
         final String erreurSurLeDoc4 = doc4Errors.get(0).getLibelle();
         assertThat(erreurSurLeDoc4, containsString(montantErrorPartString));

         LOGGER.info("Validation Du nombre de documents intégrés");
         Assert.assertEquals(8, (int) resultat.getInitialDocumentsCount());
         Assert.assertEquals(4, (int) resultat.getIntegratedDocumentsCount());
         LOGGER.info("Fin Validation du fichier résultat!");
      }
      catch (final Exception e) {
         e.printStackTrace();
         fail(e.getMessage());
      }
      finally {
         LOGGER.info("Suppression des documents");
         for (int i = 0; i < 8; i++) {
            CleanHelper.deleteOneDocument(gntService, uuids.get(i));
            CleanHelper.deleteOneDocument(gnsService, uuids.get(i));
         }
      }
   }

}
