
package sae.integration.manual;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.TransfertRequestType;
import sae.integration.webservice.modele.TransfertResponseType;

public class TransfertTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(TransfertTest.class);


   // @Ignore
   @Test
   /**
    * Lance un transfert de masse, pour rattrapage
    */
   public void transfertWattTest() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.FRONTAL_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.GNS_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForWattGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final TransfertRequestType request = new TransfertRequestType();
      request.setUuid("B42BC344-AFAC-4F1E-BF10-26FA5684547B");
      try {
         LOGGER.info("Lancement du transfert");
         final TransfertResponseType response = service.transfert(request);
         LOGGER.info("Transfert terminé");

      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }
   }

   @Test
   /**
    * Lance des transferts de documents à partir d'un fichier contenant une liste d'UUID
    * fichier liste_uuid.txt à mettre dans c:/temp/liste_uuid_transfert.txt, un UUID par ligne
    */
   public void transfertListeUUIDTest() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.FRONTAL_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.GNS_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNS_CSPP.getUrl());
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForWattGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      BufferedReader reader = null;
      String ligne;
      try {
         reader = new BufferedReader(new FileReader("c:/temp/liste_uuid_transfert.txt"));
      }
      catch (final FileNotFoundException exc) {
         sysout.println("Erreur d'ouverture");
      }

      int cpt = 0;
      while ((ligne = reader.readLine()) != null) {
         cpt++;
         final TransfertRequestType request = new TransfertRequestType();
         request.setUuid(ligne);
         try {
            service.transfert(request);
            sysout.println(ligne + ";Transfert OK");
            System.out.println(cpt + ";" + ligne + ";Transfert OK");

         }
         catch (final SOAPFaultException e) {
            sysout.println(ligne + ";Transfert KO;" + e.getMessage());
            System.out.println(cpt + ";" + ligne + ";Transfert KO");
            LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         }
      }

      sysout.close();
      reader.close();

   }


   @Test
   /**
    * Lance des transferts en multithread de documents à partir d'un fichier contenant une liste d'UUID
    * fichier liste_uuid.txt à mettre dans c:/temp/liste_uuid_transfert.txt, un UUID par ligne
    */
   public void transfertMutithreadListeUUIDTest() throws Exception {

      final SaeServicePortType service = SaeServiceStubFactory.getServiceForWattGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      BufferedReader reader = null;
      String uuid;
      try {
         reader = new BufferedReader(new FileReader("c:/temp/liste_uuid_transfert.txt"));
      }
      catch (final FileNotFoundException exc) {
         sysout.println("Erreur d'ouverture");
      }


      final ExecutorService executor = Executors.newFixedThreadPool(5);

      while ((uuid = reader.readLine()) != null) {

         // final TransfertRunnable transfertRun = new TransfertRunnable(uuid, service, sysout, LOGGER);
         // executor.execute(transfertRun);
      }
      executor.shutdown();
      executor.awaitTermination(2, TimeUnit.HOURS);

   }

}