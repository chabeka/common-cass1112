package fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.Map.Entry;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsParentFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.LogInMessageHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.VIHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.utils.LogUtils;

/**
 * Comportement d'un test d'appel à une opération de service web, pour le cas où
 * on s'attend à obtenir une réponse (pas de SoapFault)
 */
public final class WsTestListenerImplReponseAttendue implements WsTestListener {

   @Override
   public void onSetStatusInitialResultatTest(ResultatTest resultatTest) {
      resultatTest.setStatus(TestStatusEnum.NonLance);
   }

   @Override
   public void onRetourWsSansErreur(ResultatTest resultatTest,
         ConfigurationContext configurationContext,
         TestWsParentFormulaire testWsParentFormulaire) {

      String messageIn = (String) configurationContext
            .getProperty(LogInMessageHandler.PROP_MESSAGE_IN);
      String messageOut = (String) configurationContext
            .getProperty(VIHandler.PROP_MESSAGE_OUT);

      testWsParentFormulaire.getSoapFormulaire().setMessageIn(messageIn);
      testWsParentFormulaire.getSoapFormulaire().setMessageOut(messageOut);
      // Test recuperer messge SOAP dans XML IHM V2
      try {
         int ind1 = messageOut.indexOf("<soapenv:Body><ns1:");
         int ind2 = messageOut
               .indexOf("xmlns:ns1=\"http://www.cirtil.fr/saeService");
         String res = messageOut.substring(ind1 + 19, ind2);
         PrintWriter writer = new PrintWriter("/appl/sae_integration/ecde_cnp69dev/SAE_INTEGRATION/requete_reponse_xml/Test"
               + testWsParentFormulaire.getNumeroTestEnCour() + "_" + res
               + "_In.xml", "UTF-8");
         writer.println(messageIn);
         writer.close();
         PrintWriter writer2 = new PrintWriter(
               "/appl/sae_integration/ecde_cnp69dev/SAE_INTEGRATION/requete_reponse_xml/Test"
                     + testWsParentFormulaire.getNumeroTestEnCour() + "_" + res
                     + "_Out.xml", "UTF-8");
         System.out.println("2");
         System.out.println(res);
         writer2.println(messageOut);
         writer2.close();
      } catch (IOException e) {
         // do something
      }
   }

   @Override
   public void onSoapFault(ResultatTest resultatTest, AxisFault faultObtenue,
         ConfigurationContext configurationContext,
         TestWsParentFormulaire testWsParentFormulaire) {

      // Le test est en échec
      resultatTest.setStatus(TestStatusEnum.Echec);

      // Log
      ResultatTestLog log = resultatTest.getLog();
      log.appendLogLn("On s'attendait à une réponse sans erreur du service web, hors on a obtenu une SoapFault :");
      log.appendLogNewLine();
      LogUtils.logSoapFault(log, faultObtenue);

      String messageIn = (String) configurationContext
            .getProperty(LogInMessageHandler.PROP_MESSAGE_IN);
      String messageOut = (String) configurationContext
            .getProperty(VIHandler.PROP_MESSAGE_OUT);

      testWsParentFormulaire.getSoapFormulaire().setMessageIn(messageIn);
      testWsParentFormulaire.getSoapFormulaire().setMessageOut(messageOut);

      // Test recuperer messge SOAP dans XML IHM V2
      try {
         int ind1 = messageOut.indexOf("<soapenv:Body><ns1:");
         int ind2 = messageOut
               .indexOf("xmlns:ns1=\"http://www.cirtil.fr/saeService");
         String res = messageOut.substring(ind1 + 19, ind2);
         PrintWriter writer = new PrintWriter("/appl/sae_integration/ecde_cnp69dev/SAE_INTEGRATION/requete_reponse_xml/Test"
               + testWsParentFormulaire.getNumeroTestEnCour() + "_" + res
               + "_In.xml", "UTF-8");
         writer.println(messageIn);
         writer.close();
         PrintWriter writer2 = new PrintWriter(
               "/appl/sae_integration/ecde_cnp69dev/SAE_INTEGRATION/requete_reponse_xml/Test"
                     + testWsParentFormulaire.getNumeroTestEnCour() + "_" + res
                     + "_Out.xml", "UTF-8");
         System.out.println("2");
         System.out.println(res);
         writer2.println(messageOut);
         writer2.close();
      } catch (IOException e) {
         // do something
      }
   }

   @Override
   public void onRemoteException(ResultatTest resultatTest,
         RemoteException exception, ConfigurationContext configurationContext,
         TestWsParentFormulaire testWsParentFormulaire) {

      // Le test est en échec
      // On met le statut du test à Echec, et on log l'exception
      SaeServiceTestService.exceptionNonPrevue(exception, resultatTest);

      String messageIn = (String) configurationContext
            .getProperty(LogInMessageHandler.PROP_MESSAGE_IN);
      String messageOut = (String) configurationContext
            .getProperty(VIHandler.PROP_MESSAGE_OUT);

      testWsParentFormulaire.getSoapFormulaire().setMessageIn(messageIn);
      testWsParentFormulaire.getSoapFormulaire().setMessageOut(messageOut);

      // Test recuperer messge SOAP dans XML IHM V2
      try {
         int ind1 = messageOut.indexOf("<soapenv:Body><ns1:");
         int ind2 = messageOut
               .indexOf("xmlns:ns1=\"http://www.cirtil.fr/saeService");
         String res = messageOut.substring(ind1 + 19, ind2);
         PrintWriter writer = new PrintWriter("/appl/sae_integration/ecde_cnp69dev/SAE_INTEGRATION/requete_reponse_xml/Test"
               + testWsParentFormulaire.getNumeroTestEnCour() + "_" + res
               + "_In.xml", "UTF-8");
         writer.println(messageIn);
         writer.close();
         PrintWriter writer2 = new PrintWriter(
               "/appl/sae_integration/ecde_cnp69dev/SAE_INTEGRATION/requete_reponse_xml/Test"
                     + testWsParentFormulaire.getNumeroTestEnCour() + "_" + res
                     + "_Out.xml", "UTF-8");
         System.out.println("2");
         System.out.println(res);
         writer2.println(messageOut);
         writer2.close();
      } catch (IOException e) {
         // do something
      }
   }

}
