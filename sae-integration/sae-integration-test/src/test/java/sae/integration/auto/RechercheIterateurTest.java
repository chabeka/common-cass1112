
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
 * Reproduit des requêtes lancées par WATT, contenant des "OR".
 * Avant le lot 1907, tous les documents n'étaient pas trouvés
 * Le test consiste à :
 * - archiver un certain nombre de documents, dont certains doivent répondre à une requête
 * - lancer une requête de recherche par itérateur, et paginer sur l'ensemble des pages
 * - vérifier que l'ensemble des documents attendus sont trouvés
 */
public class RechercheIterateurTest {

  private static SaeServicePortType service;

  private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurTest.class);

  private CleanHelper cleanHelper;

  private Instant archivageStartTime;

  private Instant archivageEndTime;

  @BeforeClass
  public static void setup() {
    // service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_CLIENT.getUrl());
    service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.MIG_GNT.getUrl());
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

    // Archivage des documents
    for (int i = 0; i < 20; i++) {
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
      request.setMetadonnees(metaList);
      request.setDataFile(TestData.getTxtFile(metaList));

      SoapBuilder.setMetaValue(metaList, "CodeOrganismeProprietaire", "UR827");
      SoapBuilder.setMetaValue(metaList, "StatutWATT", "PRET");

      String codeProduit, codeTraitement;
      if (i % 2 == 0) {
        // Document répondant à la requête
        codeProduit = RandomData.getElementInArray(new String[] {"PC77A", "PC66A"});
        codeTraitement = RandomData.getElementInArray(new String[] {"RP17", "TP17"});
      } else {
        // Document ne répondant pas à la requête
        codeProduit = RandomData.getElementInArray(new String[] {"NC18", "NC19"});
        codeTraitement = RandomData.getElementInArray(new String[] {"PC20", "PC21"});
      }
      SoapBuilder.setMetaValue(metaList, "CodeProduitV2", codeProduit);
      SoapBuilder.setMetaValue(metaList, "CodeTraitementV2", codeTraitement);

      // Lancement de l'archivage
      ArchivageUtils.sendArchivageUnitaire(service, request, cleanHelper);
    }
    archivageEndTime = Instant.now().plusSeconds(30);
  }

  @Test
  /**
   * Reproduit une requête lancée par WATT
   */
  public void rechercheIterateurWattTest() throws Exception {
    // Archivage des documents
    LOGGER.info("Archivage des documents");
    setContext();

    // Préparation rechercher par itérateur
    final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
    final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
    final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
    SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
    SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR827");
    SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");
    SoapBuilder.addMeta(fixedMetadatas, "CodeProduitV2", "PC77A");
    SoapBuilder.addMeta(fixedMetadatas, "CodeProduitV2", "PC66A");
    SoapBuilder.addMeta(fixedMetadatas, "CodeTraitementV2", "RP17");
    SoapBuilder.addMeta(fixedMetadatas, "CodeTraitementV2", "TP17");

    mainRequest.setFixedMetadatas(fixedMetadatas);
    final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeDateTimeMetadata("DateArchivage", archivageStartTime, archivageEndTime);
    mainRequest.setVaryingMetadata(varyingMetadata);
    request.setRequetePrincipale(mainRequest);
    final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
    metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
    metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
    metadataToReturn.getMetadonneeCode().add("DateArchivage");
    request.setMetadonnees(metadataToReturn);

    request.setNbDocumentsParPage(2);

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
