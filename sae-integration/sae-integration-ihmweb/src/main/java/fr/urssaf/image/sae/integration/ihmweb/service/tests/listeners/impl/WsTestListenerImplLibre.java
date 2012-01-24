package fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl;

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
public final class WsTestListenerImplLibre implements WsTestListener {

   @Override
   public void onSetStatusInitialResultatTest(ResultatTest resultatTest) {
      resultatTest.setStatus(TestStatusEnum.SansStatus);
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

      String messageIn = (String) configurationContext
            .getProperty(LogInMessageHandler.PROP_MESSAGE_IN);
      String messageOut = (String) configurationContext
            .getProperty(VIHandler.PROP_MESSAGE_OUT);

      testWsParentFormulaire.getSoapFormulaire().setMessageIn(messageIn);
      testWsParentFormulaire.getSoapFormulaire().setMessageOut(messageOut);

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

      String messageIn = (String) configurationContext
            .getProperty(LogInMessageHandler.PROP_MESSAGE_IN);
      String messageOut = (String) configurationContext
            .getProperty(VIHandler.PROP_MESSAGE_OUT);

      testWsParentFormulaire.getSoapFormulaire().setMessageIn(messageIn);
      testWsParentFormulaire.getSoapFormulaire().setMessageOut(messageOut);

   }

}
