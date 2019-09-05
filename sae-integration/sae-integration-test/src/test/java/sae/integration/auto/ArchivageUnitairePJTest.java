package sae.integration.auto;

import java.io.IOException;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environments;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ArchivageUnitairePJResponseType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Le test consiste à archiver un document, avec ses métadonnées.
 * Le document est ensuite consulté pour vérification du binaire et des métadonnées
 */
public class ArchivageUnitairePJTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageUnitairePJTest.class);

   private static SaeServicePortType service;

   @BeforeClass
   public static void setup() {
      service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_INTERNE.getUrl());
   }

   /**
    * Test le service archivageUnitairePJ, en passant le fichier dans la requête SOAP
    * Optimisation MTOM désactivée
    */
   @Test
   public void archivageUnitairePJ_avecContenu_sansMtom_success() throws IOException {

      archivageUnitairePJ_avecContenu(false);

   }

   /**
    * Test le service archivageUnitairePJ, en passant le fichier dans la requête SOAP
    * Optimisation MTOM activée
    */
   @Test
   public void archivageUnitairePJ_avecContenu_avecMtom_success() throws IOException {

      archivageUnitairePJ_avecContenu(true);

   }

   private void archivageUnitairePJ_avecContenu(final boolean withMtom) throws IOException {

      // Requête
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      request.setMetadonnees(RandomData.getRandomMetadatas());
      request.setDataFile(TestData.getTiffFile(request.getMetadonnees()));

      // Activation de l'optimisation MTOM si demandée
      if (withMtom) {
         final BindingProvider bp = (BindingProvider) service;
         final SOAPBinding binding = (SOAPBinding) bp.getBinding();
         binding.setMTOMEnabled(withMtom);
      }

      ArchivageUnitairePJResponseType response;

      response = service.archivageUnitairePJ(request);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idDoc = response.getIdArchive();
      LOGGER.info("Archivage en succès. UUID reçu : {}", idDoc);
      // Validation
      ArchivageValidationUtils.validateDocument(service, idDoc, request.getMetadonnees());

      // Nettoyage
      CleanHelper.deleteOneDocument(service, idDoc);
   }


}
