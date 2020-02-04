
package sae.integration.auto;

import java.time.Instant;
import java.util.UUID;

import javax.xml.ws.soap.SOAPFaultException;

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
import sae.integration.util.SoapHelper;
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
 * Test de la recherche par itérateur, sur le frontal, en faisant en sorte de faire intervenir plusieurs instances
 */
public class RechercheIterateurMultiInstance {

   private static SaeServicePortType service;

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurMultiInstance.class);

   private CleanHelper cleanHelper;

   private Instant archivageStartTime;

   private Instant archivageEndTime;

   private String randomString;

   @BeforeClass
   public static void setup() {
      service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.FRONTAL_LOCAL.getUrl());
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
    * On archive 10 documents, sur deux instances différentes
    */
   private void setContext(final int[] counts) {
      // Récupération de l'heure courante (avec de la marge en cas de désynchronisation d'horloge)
      archivageStartTime = Instant.now().minusSeconds(30);
      randomString = UUID.randomUUID().toString();
      final String[] orgas = new String[] {"UR556", "UR557"};
      for (int orgaIndex = 0; orgaIndex < 2; orgaIndex++) {
         // Archivage des documents
         for (int i = 0; i < counts[orgaIndex]; i++) {
            final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
            final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
            request.setMetadonnees(metaList);
            request.setDataFile(TestData.getTxtFile(metaList));

            SoapBuilder.setMetaValue(metaList, "CodeOrganismeProprietaire", orgas[orgaIndex]);
            SoapBuilder.setMetaValue(metaList, "NumeroPli", randomString);

            // Lancement de l'archivage
            ArchivageUtils.sendArchivageUnitaire(service, request, cleanHelper);
         }
      }

      archivageEndTime = Instant.now().plusSeconds(30);
   }

   @Test
   public void rechercheIterateurTest1() throws Exception {
      rechercheIterateur(new int[] {5, 5});
   }

   @Test
   public void rechercheIterateurTest2() throws Exception {
      rechercheIterateur(new int[] {1, 9});
   }

   @Test
   public void rechercheIterateurTest3() throws Exception {
      rechercheIterateur(new int[] {10, 0});
   }

   public void rechercheIterateur(final int[] counts) throws Exception {
      // Archivage des documents
      LOGGER.info("Archivage des documents");
      setContext(counts);

      // Préparation rechercher par itérateur
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "NumeroPli", randomString);

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeDateTimeMetadata("DateArchivage", archivageStartTime, archivageEndTime);
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("NumeroPli");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(2);

      // Boucle sur les différentes pages
      LOGGER.info("Parcours des documents");
      int counter = 0;
      while (true) {
         RechercheParIterateurResponseType response = null;
         try {
            response = service.rechercheParIterateur(request);
         }
         catch (final SOAPFaultException e) {
            LOGGER.warn(e.getMessage());
            LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
            throw e;
         }

         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            LOGGER.debug("Doc trouvé : {}", UUID);
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      LOGGER.info("Nombre de documents trouvés : {}", counter);
      Assert.assertEquals("On s'attend à trouver 10 documents", 10, counter);
   }
}
