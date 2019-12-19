package sae.integration.manual;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.util.ArchivageUtils;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Lance des archivage unitaires en boucle, afin de créer un jeu de données sur l'environnement cible
 */
public class ArchivageUnitaireEnBoucleTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageUnitaireEnBoucleTest.class);

  private static SaeServicePortType service;

  private static Environment environment;

  @BeforeClass
  public static void setup() {
    environment = Environments.GNT_INT_PAJE;
    service = SaeServiceStubFactory.getServiceForDevToutesActions(environment.getUrl());
    // service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(environment.getUrl());
  }


  @Test
  public void datasetCreationTest() {

    final int iterationsCount = 100;

    for (int i = 0; i < iterationsCount; i++) {
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      request.setMetadonnees(RandomData.getRandomMetadatas());
      request.setDataFile(TestData.getTxtFile(request.getMetadonnees()));
      final String uuid = ArchivageUtils.sendArchivageUnitaire(service, request);
      LOGGER.info("UUID : {}", uuid);
    }
  }

}
