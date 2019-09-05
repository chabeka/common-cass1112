package sae.integration.auto.transfert.unitaire;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
 * Permet de tester le comportement lors de la tentative de transfert concurrent du même document.
 * Description du test :
 * - on archive un document en GNT
 * - on lance plusieurs threads tentant de transférer le document
 * - au final, on vérifie que le document bien transféré (présent en GNS et plus en GNT)
 */
public class TransfertUnitaireMultithread {

   private static SaeServicePortType gntService;

   private static SaeServicePortType gnsService;

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertUnitaireMultithread.class);

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
   public void transfertMutithreadTest() throws Exception {
      try {
         docId = ArchivageUtils.archivagePDF(gntService);
      }
      catch (final SOAPFaultException e) {
         LOGGER.warn(e.getMessage());
         LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

      LOGGER.info("Archivage d'un document en GNT : {}", docId);

      final ExecutorService executor = Executors.newFixedThreadPool(20);
      threadId = 0;
      for (int i = 0; i < 10; i++) {
         final Runnable runnable = () -> {
            final int currentThreadId = threadId++;
            final TransfertRequestType transfertRequest = new TransfertRequestType();
            transfertRequest.setUuid(docId);
            try {
               LOGGER.debug("Lancement du transfert sur le thread n°{}", currentThreadId);
               gntService.transfert(transfertRequest);
               LOGGER.debug("Fin du transfert sur le thread n°{}", currentThreadId);
            }
            catch (final SOAPFaultException e) {
               if (!e.getMessage().contains("ArchiveNonTrouvee") && !e.getMessage().contains("a déjà été transféré")) {
                  LOGGER.warn("Erreur inattendue lors de la tentative de transfert :", e);
                  LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
               }
            }
         };
         executor.execute(runnable);
      }
      executor.shutdown();
      executor.awaitTermination(2, TimeUnit.HOURS);

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
