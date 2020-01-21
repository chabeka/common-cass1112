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
import sae.integration.util.SoapBuilder;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.DataFileType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Lance un archivage unitaire pour créer un jeu de données pour une MOE cliente
 */
public class ArchivageUnitaireJeuDonneesTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageUnitaireJeuDonneesTest.class);

   private static SaeServicePortType service;

   private static Environment environment;

   @BeforeClass
   public static void setup() {
      environment = Environments.GNS_INT_CLIENT;
      service = SaeServiceStubFactory.getServiceForDevToutesActions(environment.getUrl());
   }


   @Test
   public void datasetCreationTest() {

      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      request.setMetadonnees(RandomData.getRandomMetadatas());
      final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
      SoapBuilder.setMetaValue(metaList, "NumeroCompteExterne", "727000000652922071");
      SoapBuilder.setMetaValue(metaList, "Siren", "833103286");
      SoapBuilder.setMetaValue(metaList, "CodeRND", "2.3.1.1.17");
      final DataFileType file = TestData.getFileFromPath("C:\\temp\\attestation-de-radiation.pdf", "fmt/354", "1", metaList);
      request.setDataFile(file);
      request.setMetadonnees(metaList);
      final String uuid = ArchivageUtils.sendArchivageUnitaire(service, request);
      LOGGER.info("UUID : {}", uuid);
   }

}
