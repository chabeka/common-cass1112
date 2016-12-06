package fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsParentFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.LogInMessageHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.VIHandler;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.utils.LogUtils;

/**
 * Comportement d'un test d'appel à une opération de service web, pour le cas où
 * on ne s'attend pas à un comportement particulier
 */
public final class WsTestListenerImplSansSoapFault implements WsTestListener {

   @Override
   public void onSetStatusInitialResultatTest(ResultatTest resultatTest) {
      resultatTest.setStatus(TestStatusEnum.SansStatus);
   }

   @Override
   public void onRetourWsSansErreur(ResultatTest resultatTest,
         ConfigurationContext configurationContext,
         TestWsParentFormulaire testWsParentFormulaire) {
      resultatTest.setStatus(TestStatusEnum.Succes);

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

      // On loggue simplement la SoapFault
      ResultatTestLog log = resultatTest.getLog();
      log.appendLogLn("On a obtenu une SoapFault :");
      log.appendLogNewLine();
      LogUtils.logSoapFault(log, faultObtenue);
      resultatTest.setStatus(TestStatusEnum.Echec);

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

      // On loggue simplement l'exception
      ResultatTestLog log = resultatTest.getLog();
      log.appendLogLn("Une exception non SoapFault a été levée :");
      log.appendLogNewLine();
      log.appendLogLn(exception.toString());
      resultatTest.setStatus(TestStatusEnum.Echec);

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
