
package sae.integration.auto;

import java.time.Instant;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environments;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapBuilder;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.IdentifiantPageType;
import sae.integration.webservice.modele.ListeMetadonneeCodeType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.RangeMetadonneeType;
import sae.integration.webservice.modele.RechercheParIterateurRequestType;
import sae.integration.webservice.modele.RechercheParIterateurResponseType;
import sae.integration.webservice.modele.RequetePrincipaleType;
import sae.integration.webservice.modele.ResultatRechercheType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test de la recherche par itérateur.
 * On reproduit le comportement de l'application SCRIBE : recherche par itérateur
 * et suppression des documents trouvés en cours d'itération.
 * Le test consiste à :
 * - archiver un certain nombre de documents
 * - lancer une requête de recherche par itérateur, et paginer sur l'ensemble des pages
 * - à chaque page : supprimer les documents trouvés
 * - à la fin : on s'assure que tous les documents attendus ont été parcourus et supprimés
 */
public class RechercheIterateurAndDeletionTest {

   private static SaeServicePortType service;

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurAndDeletionTest.class);

   private CleanHelper cleanHelper;

   private Instant archivageStartTime;

   private Instant archivageEndTime;

   private String codeProduitV2;

   @BeforeClass
   public static void setup() {
      // service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_CLIENT.getUrl());
      // service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_INT_PAJE.getUrl());
      service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_INT_CESU.getUrl());
   }

   @Before
   public void before() {
      cleanHelper = new CleanHelper(service);
   }

   @After
   public void after() throws Exception {
      LOGGER.info("Nettoyage...");
      cleanHelper.close();
   }

   /**
    * Mise en place du contexte
    * On archive 20 documents, dont 10 répondent à la requête
    */
   private void setContext() {
      // Récupération de l'heure courante (avec de la marge en cas de désynchronisation d'horloge)
      archivageStartTime = Instant.now().minusSeconds(30);
      // On fixe un codeProduitV2 qui nous sert à bien identifier les documents
      codeProduitV2 = RandomData.getRandomString(5);

      // Archivage des documents
      for (int i = 0; i < 20; i++) {
         final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
         final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
         request.setMetadonnees(metaList);
         request.setDataFile(TestData.getTxtFile(metaList));

         SoapBuilder.setMetaValue(metaList, "CodeOrganismeProprietaire", "UR827");
         SoapBuilder.setMetaValue(metaList, "ApplicationProductrice", "SCRIBE");
         SoapBuilder.setMetaValue(metaList, "ATransfererScribe", "true");
         SoapBuilder.setMetaValue(metaList, "CodeProduitV2", codeProduitV2);

         // Lancement de l'archivage
         ArchivageUtils.sendArchivageUnitaire(service, request, cleanHelper);
      }
      archivageEndTime = Instant.now().plusSeconds(30);
   }

   @Test
   /**
    * Reproduit une requête lancée par Scribe
    */
   public void rechercheIterateurScribeTest() throws Exception {
      // Archivage des documents
      LOGGER.info("Archivage des documents");
      setContext();

      // Préparation rechercher par itérateur
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR827");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationProductrice", "SCRIBE");
      SoapBuilder.addMeta(fixedMetadatas, "ATransfererScribe", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeProduitV2", codeProduitV2);

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeDateTimeMetadata("DateArchivage", archivageStartTime, archivageEndTime);
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(3);

      // Boucle sur les différentes pages
      LOGGER.info("Parcours des documents");
      int counter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            LOGGER.debug("Doc trouvé : {}", UUID);
            counter++;
            // Suppression du doc
            CleanHelper.deleteOneDocument(service, UUID);
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      LOGGER.info("Nombre de documents trouvés : {}", counter);
      Assert.assertEquals("On s'attend à trouver 20 documents", 20, counter);

   }
}
