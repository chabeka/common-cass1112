package sae.integration.auto.transfert.unitaire;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.ArchivageValidationUtils;
import sae.integration.util.CleanHelper;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.TransfertRequestType;

/**
 * Permet de tester un simple transfet unitaire
 * Description du test :
 * - on archive un document en GNT
 * - on lance son transfert
 * - au final, on vérifie que le document bien transféré (présent en GNS et plus en GNT)
 */
public class TransfertUnitaireOKTest {

   private static SaeServicePortType gntService;

   private static SaeServicePortType gnsService;

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertUnitaireOKTest.class);

   private volatile int threadId;

   private String docId;

   @BeforeClass
   public static void setup() {
      // gntService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_INTERNE.getUrl());
      // gnsService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_INT_INTERNE.getUrl());
      // gntService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.LOCALHOST.getUrl());
      // gnsService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_INT_CLIENT.getUrl());
      gntService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_PIC.getUrl());
      gnsService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_PIC.getUrl());
      // gntService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_PAJE.getUrl());
      // gnsService = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_INT_PAJE.getUrl());
   }

   @After
   public void after() throws Exception {
      LOGGER.info("Nettoyage...");
      if (docId != null) {
         CleanHelper.deleteOneDocument(gntService, docId);
         CleanHelper.deleteOneDocument(gnsService, docId);
      }
   }

   @Test
   public void transfertTest() throws Exception {

      docId = ArchivageUtils.archivagePDF(gntService);
      LOGGER.info("Archivage d'un document en GNT : {}", docId);
      final TransfertRequestType transfertRequest = new TransfertRequestType();
      transfertRequest.setUuid(docId);
      try {
         LOGGER.info("Lancement du transfert");
         gntService.transfert(transfertRequest);
         LOGGER.info("Fin du transfert");
      }
      catch (final SOAPFaultException e) {
         LOGGER.warn("Erreur inattendue lors de la tentative de transfert :", e);
         LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

      LOGGER.info("Temporisation de 2 secondes");
      Thread.sleep(2000);
      LOGGER.info("Test d'existence en GNT et GNS", docId);
      final boolean existsInGNT = ArchivageValidationUtils.docExists(gntService, docId);
      final boolean existsInGNS = ArchivageValidationUtils.docExists(gnsService, docId);
      LOGGER.info("Présence en GNT : {}", existsInGNT);
      LOGGER.info("Présence en GNS : {}", existsInGNS);

      Assert.assertEquals("Le document ne doit plus exister en GNT", false, existsInGNT);
      Assert.assertEquals("Le document doit exister en GNS", true, existsInGNS);
   }


}
