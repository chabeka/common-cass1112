package sae.integration.auto;

import java.io.IOException;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.environment.Environment;
import sae.integration.environment.Environments;
import sae.integration.job.JobManager;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapBuilder;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ArchivageUnitairePJResponseType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Le test consiste à archiver un document, avec ses métadonnées.
 * Le document est ensuite consulté pour vérification du binaire et des métadonnées
 */
public class ArchivageUnitairePJTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageUnitairePJTest.class);

   private static SaeServicePortType service;

   private static Environment environment;

   @BeforeClass
   public static void setup() {
      // environment = Environments.GNT_INT_PAJE;
      // environment = Environments.GNS_INT_INTERNE;
      // environment = Environments.GNT_INT_INTERNE;
      environment = Environments.FRONTAL_DEV;
      // environment = Environments.GNT_INT_CLIENT;
      // environment = Environments.GNS_INT_CLIENT;
      service = SaeServiceStubFactory.getServiceForDevToutesActions(environment.getUrl());
      // service = SaeServiceStubFactory.getServiceForCimeGNSCotisant(environment.getUrl());
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
      final ListeMetadonneeType metas = RandomData.getRandomMetadatas();
      SoapBuilder.setMetaValue(metas, "ApplicationTraitement", "CIME");
      SoapBuilder.setMetaValue(metas, "ApplicationProductrice", "ALADIN");
      request.setMetadonnees(metas);
      request.setDataFile(TestData.getTiffFile(request.getMetadonnees()));

      // Activation de l'optimisation MTOM si demandée
      if (withMtom) {
         final BindingProvider bp = (BindingProvider) service;
         final SOAPBinding binding = (SOAPBinding) bp.getBinding();
         binding.setMTOMEnabled(withMtom);
      }

      ArchivageUnitairePJResponseType response;

      try {
         response = service.archivageUnitairePJ(request);
      }
      catch (final SOAPFaultException e) {
         LOGGER.warn(e.getMessage());
         LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idDoc = response.getIdArchive();
      LOGGER.info("Archivage en succès. UUID reçu : {}", idDoc);
      // Validation
      ArchivageValidationUtils.validateDocument(service, idDoc, request.getMetadonnees());

      // Nettoyage
      CleanHelper.deleteOneDocument(service, idDoc);
   }

   /**
    * Test le service archivageUnitairePJ, en passant le fichier sur l'ecde
    * 
    * @throws Exception
    */
   @Test
   public void archivageUnitairePJ_ECDE() throws Exception {

      // Envoi d'un document sur l'ecde
      String ecdeUrl;
      try (JobManager manager = new JobManager(environment)) {
         manager.createDocDirInECDE();
         ecdeUrl = manager.sendDocumentInECDE("documents/testDoc.tif", "testDoc.tif");
         LOGGER.info("Chemin du document sur l'ecde : {}", ecdeUrl);
      }

      // Requête
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      request.setMetadonnees(RandomData.getRandomMetadatas());
      TestData.updateMetaForTiffFile(request.getMetadonnees());
      request.setEcdeUrl(ecdeUrl);

      // On spécifie le HASH en SHA-256
      final byte[] contenu = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("documents/testDoc.tif"));
      final ListeMetadonneeType metaList = request.getMetadonnees();
      SoapBuilder.setMetaValue(metaList, "Hash", DigestUtils.sha256Hex(contenu));
      SoapBuilder.setMetaValue(metaList, "TypeHash", "SHA-256");

      ArchivageUnitairePJResponseType response;

      try {
         response = service.archivageUnitairePJ(request);
      }
      catch (final SOAPFaultException e) {
         LOGGER.warn(e.getMessage());
         LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idDoc = response.getIdArchive();
      LOGGER.info("Archivage en succès. UUID reçu : {}", idDoc);
      // Validation
      ArchivageValidationUtils.validateDocument(service, idDoc, request.getMetadonnees());

      // Nettoyage
      CleanHelper.deleteOneDocument(service, idDoc);
   }

   /**
    * Test le service archivageUnitairePJ, en utilisant un hash de type SHA256
    * 
    * @throws Exception
    */
   @Test
   public void archivageUnitairePJ_SHA256() throws Exception {

      // Requête
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      request.setMetadonnees(RandomData.getRandomMetadatas());
      request.setDataFile(TestData.getTiffFile(request.getMetadonnees()));

      // On spécifie le HASH en SHA-256
      final byte[] contenu = TestData.getTiffFileContent();
      final ListeMetadonneeType metaList = request.getMetadonnees();
      SoapBuilder.setMetaValue(metaList, "Hash", DigestUtils.sha256Hex(contenu));
      SoapBuilder.setMetaValue(metaList, "TypeHash", "SHA-256");

      ArchivageUnitairePJResponseType response;

      try {
         response = service.archivageUnitairePJ(request);
      }
      catch (final SOAPFaultException e) {
         LOGGER.warn(e.getMessage());
         LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idDoc = response.getIdArchive();
      LOGGER.info("Archivage en succès. UUID reçu : {}", idDoc);
      // Validation
      // ArchivageValidationUtils.validateDocument(service, idDoc, request.getMetadonnees());

      // Nettoyage
      CleanHelper.deleteOneDocument(service, idDoc);
   }

}
